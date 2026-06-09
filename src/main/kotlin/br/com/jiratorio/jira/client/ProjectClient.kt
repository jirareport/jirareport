package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.external.ExternalBoard
import br.com.jiratorio.jira.domain.JiraStatusList
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface ProjectClient {

    @GetExchange("/rest/api/2/project")
    fun findAll(): List<ExternalBoard>

    @GetExchange("/rest/api/2/project/{projectId}/statuses")
    fun findStatusFromProject(@PathVariable("projectId") projectId: Long): List<JiraStatusList>

    @GetExchange("/rest/api/2/project/{projectId}")
    fun findById(@PathVariable("projectId") projectId: Long): ExternalBoard

}
