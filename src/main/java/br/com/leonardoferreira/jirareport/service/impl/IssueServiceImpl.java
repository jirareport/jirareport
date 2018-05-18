package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.LeadTimeService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author lferreira
 * @since 5/7/18 8:01 PM
 */
@Slf4j
@Service
public class IssueServiceImpl extends AbstractService implements IssueService {

    @Autowired
    private IssueClient issueClient;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ChartService chartService;

    @Autowired
    private LeadTimeService leadTimeService;

    @Override
    @Transactional
    public List<Issue> findAllInJira(final IssuePeriodId issuePeriodId) {
        final Project project = projectService.findById(issuePeriodId.getProjectId());

        String issuesStr = issueClient.findAll(currentToken(), buildJQL(issuePeriodId, project));

        List<Issue> issues = issueMapper.parse(issuesStr, project);
        issueRepository.saveAll(issues);

        leadTimeService.createLeadTimes(issues, project.getId());

        return issues;
    }

    @Override
    @Transactional(readOnly = true)
    public SandBox findByExample(final Long projectId, final IssueForm issueForm) {
        log.info("Method=findByExample, projectId={}, issueForm={}", projectId, issueForm);

        if (issueForm.getStartDate() == null || issueForm.getEndDate() == null) {
            return new SandBox();
        }

        List<Issue> issues = issueRepository.findByExample(projectId, issueForm);
        final ChartAggregator chartAggregator = chartService.buildAllCharts(issues);

        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        final SandBox sandBox = new SandBox();
        sandBox.setIssues(issues);
        sandBox.setChartAggregator(chartAggregator);
        sandBox.setAvgLeadTime(avgLeadTime);

        return sandBox;
    }

    @Override
    public SandBoxFilter findSandBoxFilters(final Long projectId, final SandBox sandBox, final IssueForm issueForm) {
        final SandBoxFilter sandBoxFilter = new SandBoxFilter();

        sandBoxFilter.setEstimatives(issueRepository.findAllEstimativesByProjectId(projectId));
        sandBoxFilter
                .setKeys(Stream.concat(sandBox.getIssues().stream().map(Issue::getKey), issueForm.getKeys().stream())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList()));
        sandBoxFilter.setSystems(issueRepository.findAllSystemsByProjectId(projectId));
        sandBoxFilter.setEpics(issueRepository.findAllEpicsByProjectId(projectId));
        sandBoxFilter.setIssueTypes(issueRepository.findAllIssueTypesByProjectId(projectId));
        sandBoxFilter.setProjects(issueRepository.findAllIssueProjectsByProjectId(projectId));

        return sandBoxFilter;
    }

    @Override
    public Histogram calcHistogramData(final List<Issue> issues) {
        if (issues == null || issues.size() < 10) {
            return null;
        }
        final int totalElements = issues.size();
        issues.sort((a, b) -> a.getLeadTime().compareTo(b.getLeadTime()));
        int median = calculateCeilingPercentage(totalElements, 50);
        int percentile75 = calculateCeilingPercentage(totalElements, 75);
        int percentile90 = calculateCeilingPercentage(totalElements, 90);

        return new Histogram(issues.get(median - 1).getLeadTime(), issues.get(percentile75 - 1).getLeadTime(),
                issues.get(percentile90 - 1).getLeadTime());
    }

    @Override
    public List<Issue> findByIssuePeriodId(final IssuePeriodId issuePeriodId) {
        log.info("Method=findByIssuePeriodId, issuePeriodId={}", issuePeriodId);
        return issueRepository.findByIssuePeriodId(issuePeriodId.getProjectId(), issuePeriodId.getStartDate(), issuePeriodId.getEndDate());
    }

    private int calculateCeilingPercentage(final int totalElements, final int percentage) {
        return new BigDecimal((double) totalElements * percentage / 100).setScale(0, RoundingMode.CEILING)
                .intValue();
    }

    private String buildJQL(final IssuePeriodId issuePeriodId, final Project project) {
        StringBuilder jql = new StringBuilder();
        jql.append("project = ").append(project.getId()).append(" ");
        if (!CollectionUtils.isEmpty(project.getIgnoreIssueType())) {
            jql.append(" AND issuetype not in (");
            jql.append(String.join(",", project.getIgnoreIssueType()
                    .stream()
                    .map(i -> "'" + i + "'")
                    .collect(Collectors.toList()))).append(" ) ");
        }
        jql.append("AND (STATUS CHANGED TO '").append(project.getEndColumn()).append("' DURING('");
        jql.append(DateUtil.toENDate(issuePeriodId.getStartDate())).append("', '");
        jql.append(DateUtil.toENDate(issuePeriodId.getEndDate())).append(" 23:59')");
        jql.append("OR ( Resolved >= ");
        jql.append(DateUtil.toENDate(issuePeriodId.getStartDate()));
        jql.append(" AND Resolved <= '");
        jql.append(DateUtil.toENDate(issuePeriodId.getEndDate())).append(" 23:59'");
        jql.append(" AND NOT STATUS CHANGED TO '").append(project.getEndColumn()).append("' ");
        jql.append("   )");
        jql.append(")");

        return jql.toString();
    }
}
