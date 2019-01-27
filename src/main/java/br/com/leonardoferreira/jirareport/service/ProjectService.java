package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.vo.BoardStatusList;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import java.util.List;

public interface ProjectService {

    List<JiraProject> findAllJiraProject();

    List<BoardStatusList> findStatusFromProject(Long projectId);

    JiraProject findById(Long projectId);

}
