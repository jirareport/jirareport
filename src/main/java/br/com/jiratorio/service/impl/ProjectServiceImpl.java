package br.com.jiratorio.service.impl;

import br.com.jiratorio.client.ProjectClient;
import br.com.jiratorio.domain.jira.BoardStatusList;
import br.com.jiratorio.domain.jira.JiraProject;
import br.com.jiratorio.service.ProjectService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectClient projectClient;

    public ProjectServiceImpl(final ProjectClient projectClient) {
        this.projectClient = projectClient;
    }

    @Override
    public List<JiraProject> findAllJiraProject() {
        log.info("Method=findAllJiraProject");
        return projectClient.findAll();
    }

    @Override
    public List<BoardStatusList> findStatusFromProject(final Long projectId) {
        log.info("Method=findStatusFromProject, projectId={}", projectId);
        return projectClient.findStatusFromProject(projectId);
    }

    @Override
    public JiraProject findById(final Long projectId) {
        log.info("Method=findById, projectId={}", projectId);
        return projectClient.findById(projectId);
    }

}
