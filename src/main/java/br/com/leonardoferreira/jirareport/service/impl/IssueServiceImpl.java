package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author s2it_leferreira
 * @since 5/7/18 8:01 PM
 */
@Slf4j
@Service
public class IssueServiceImpl extends AbstractService implements IssueService {

    private final IssueClient issueClient;

    private final ProjectService projectService;

    private final IssueMapper issueMapper;

    private final IssueRepository issueRepository;

    private final ChartService chartService;

    public IssueServiceImpl(final IssueClient issueClient, final ProjectService projectService,
                            final IssueMapper issueMapper, final IssueRepository issueRepository,
                            final ChartService chartService) {
        this.issueClient = issueClient;
        this.projectService = projectService;
        this.issueMapper = issueMapper;
        this.issueRepository = issueRepository;
        this.chartService = chartService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> findAllInJira(final IssuePeriodId issuePeriodId) {
        final Project project = projectService.findById(issuePeriodId.getProjectId());

        String issues = issueClient.findAll(currentToken(), buildJQL(issuePeriodId, project));

        return issueMapper.parse(issues, project);
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
    @Transactional
    public void saveAll(final List<Issue> issues) {
        log.info("Method=saveAll, issues={}", issues);
        issueRepository.saveAll(issues);
    }

    private String buildJQL(final IssuePeriodId issuePeriodId, final Project project) {
        StringBuilder jql = new StringBuilder();
        jql.append("project = ").append(project.getId()).append(" ");
        jql.append("AND STATUS CHANGED TO \"").append(project.getEndColumn()).append("\" DURING(\"");
        jql.append(DateUtil.toENDate(issuePeriodId.getStartDate())).append("\", \"");
        jql.append(DateUtil.toENDate(issuePeriodId.getEndDate())).append("\")");

        return jql.toString();
    }
}
