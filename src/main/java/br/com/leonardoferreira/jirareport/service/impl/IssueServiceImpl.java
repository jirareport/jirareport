package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.IssueForm;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public IssueServiceImpl(final IssueClient issueClient, final ProjectService projectService, IssueMapper issueMapper) {
        this.issueClient = issueClient;
        this.projectService = projectService;
        this.issueMapper = issueMapper;
    }

    @Override
    public List<Issue> findAllInJira(final IssueForm issueForm) {
        final Project project = projectService.findById(issueForm.getProjectId());

        String issues = issueClient.findAll(currentToken(), buildJQL(issueForm, project));

        return issueMapper.parse(issues, project);
    }

    private String buildJQL(final IssueForm issueForm, final Project project) {
        StringBuilder jql = new StringBuilder();
        jql.append("project = ").append(project.getId()).append(" ");
        jql.append("AND STATUS CHANGED TO \"").append(project.getEndColumn()).append("\" DURING(\"");
        jql.append(DateUtil.toENDate(issueForm.getStartDate())).append("\", \"");
        jql.append(DateUtil.toENDate(issueForm.getEndDate())).append("\")");

        return jql.toString();
    }
}
