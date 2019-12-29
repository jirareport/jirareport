package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.jira.JiraProject
import br.com.jiratorio.domain.jira.JiraProjectDetails
import br.com.jiratorio.service.ProjectService
import br.com.jiratorio.usecase.project.FindAllJiraProject
import br.com.jiratorio.usecase.project.FindProject
import br.com.jiratorio.usecase.project.FindProjectStatusesByBoard
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val findAllJiraProject: FindAllJiraProject,
    private val findProject: FindProject,
    private val findProjectStatusesByBoard: FindProjectStatusesByBoard
) : ProjectService {

    override fun findAllJiraProject(): List<JiraProject> =
        findAllJiraProject.execute()

    override fun findStatusesByBoardId(boardId: Long): Set<String> =
        findProjectStatusesByBoard.execute(boardId)

    override fun findById(projectId: Long): JiraProjectDetails =
        findProject.execute(projectId)

}
