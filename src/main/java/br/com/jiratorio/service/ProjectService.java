package br.com.jiratorio.service;

import br.com.jiratorio.domain.jira.BoardStatusList;
import br.com.jiratorio.domain.jira.JiraProject;
import java.util.List;

public interface ProjectService {

    List<JiraProject> findAllJiraProject();

    List<BoardStatusList> findStatusFromProject(Long projectId);

    JiraProject findById(Long projectId);

}
