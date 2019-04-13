package br.com.jiratorio.service

import br.com.jiratorio.domain.jira.JiraProjectDetails
import br.com.jiratorio.domain.jira.JiraProject

interface ProjectService {

    fun findAllJiraProject(): List<JiraProject>

    fun findStatusesByBoardId(boardId: Long): Set<String>

    fun findById(projectId: Long): JiraProjectDetails

}
