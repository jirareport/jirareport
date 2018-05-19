package br.com.leonardoferreira.jirareport.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Issue;
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
    @ExecutionTime
    @Transactional
    public List<Issue> findAllInJira(final IssuePeriodId issuePeriodId) {
        log.info("Method=findAllInJira, issuePeriodId={}", issuePeriodId);

        final Project project = projectService.findById(issuePeriodId.getProjectId());

        String issuesStr = issueClient.findAll(currentToken(), buildJQL(issuePeriodId, project));

        List<Issue> issues = issueMapper.parse(issuesStr, project);
        issueRepository.saveAll(issues);

        leadTimeService.createLeadTimes(issues, project.getId());

        return issues;
    }

    @Override
    @ExecutionTime
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

        return SandBox.builder()
                .issues(issues)
                .chartAggregator(chartAggregator)
                .avgLeadTime(avgLeadTime)
                .build();
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public SandBoxFilter findSandBoxFilters(final Long projectId, final SandBox sandBox, final IssueForm issueForm) {
        log.info("Method=findSandBoxFilters, projectId={}, sandBox={}, issueForm={}", projectId, sandBox, issueForm);

        return SandBoxFilter.builder()
                .estimatives(issueRepository.findAllEstimativesByProjectId(projectId))
                .keys(findAllKeys(sandBox, issueForm))
                .systems(issueRepository.findAllSystemsByProjectId(projectId))
                .epics(issueRepository.findAllEpicsByProjectId(projectId))
                .issueTypes(issueRepository.findAllIssueTypesByProjectId(projectId))
                .projects(issueRepository.findAllIssueProjectsByProjectId(projectId))
                .build();
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Histogram calcHistogramData(final List<Issue> issues) {
        log.info("Method=calcHistogramData, issues={}", issues);

        if (issues == null || issues.size() < 10) {
            return null;
        }

        issues.sort(Comparator.comparing(Issue::getLeadTime));

        int totalElements = issues.size();
        int median = calculateCeilingPercentage(totalElements, 50);
        int percentile75 = calculateCeilingPercentage(totalElements, 75);
        int percentile90 = calculateCeilingPercentage(totalElements, 90);

        return Histogram.builder()
                .median(issues.get(median - 1).getLeadTime())
                .percentile75(issues.get(percentile75 - 1).getLeadTime())
                .percentile90(issues.get(percentile90 - 1).getLeadTime())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> findByIssuePeriodId(final IssuePeriodId issuePeriodId) {
        log.info("Method=findByIssuePeriodId, issuePeriodId={}", issuePeriodId);

        return issueRepository.findByIssuePeriodId(issuePeriodId.getProjectId(), issuePeriodId.getStartDate(), issuePeriodId.getEndDate());
    }

    private String buildJQL(final IssuePeriodId issuePeriodId, final Project project) {
        log.info("Method=buildJQL, issuePeriodId={}, project={}", issuePeriodId, project);

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

    private List<String> findAllKeys(final SandBox sandBox, final IssueForm issueForm) {
        return Stream.concat(sandBox.getIssues().stream().map(Issue::getKey), issueForm.getKeys().stream())
                .distinct().sorted().collect(Collectors.toList());
    }

    private int calculateCeilingPercentage(final int totalElements, final int percentage) {
        return new BigDecimal((double) totalElements * percentage / 100).setScale(0, RoundingMode.CEILING)
                .intValue();
    }
}
