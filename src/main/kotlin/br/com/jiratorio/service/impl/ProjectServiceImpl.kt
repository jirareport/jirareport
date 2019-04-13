package br.com.jiratorio.service.impl

import br.com.jiratorio.client.ProjectClient
import br.com.jiratorio.domain.jira.JiraProjectDetails
import br.com.jiratorio.domain.jira.JiraProject
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.ProjectService
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectClient: ProjectClient,
    private val boardService: BoardService
) : ProjectService {

    override fun findAllJiraProject(): List<JiraProject> {
        log.info("Method=findAllJiraProject")
        return projectClient.findAll()
    }

    override fun findStatusesByBoardId(boardId: Long): Set<String> {
        log.info("Method=findStatusesByBoardId, boardId={}", boardId)
        val board = boardService.findById(boardId)
        return projectClient.findStatusFromProject(board.externalId)
            .map { it.statuses }.flatten()
            .map { it.name }.toSet()
    }

    override fun findById(projectId: Long): JiraProjectDetails {
        log.info("Method=findById, projectId={}", projectId)
        return projectClient.findById(projectId)
            .orElseThrow(::ResourceNotFound)
    }

}
