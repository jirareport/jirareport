package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import br.com.leonardoferreira.jirareport.util.DateUtil;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    public IssueServiceImpl(final IssueClient issueClient, final ProjectService projectService,
                            final IssueMapper issueMapper, final IssueRepository issueRepository) {
        this.issueClient = issueClient;
        this.projectService = projectService;
        this.issueMapper = issueMapper;
        this.issueRepository = issueRepository;
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
    public List<Issue> findByExample(final Long projectId, final IssueForm issueForm) {
        log.info("Method=findByExample, projectId={}, issueForm={}", projectId, issueForm);
        List<Issue> issues;
        if (issueForm.getStartDate() != null && issueForm.getEndDate() != null) {
            issues = issueRepository.findByExample(projectId, issueForm);
        } else {
            issues = Collections.emptyList();
        }

        return issues;
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
