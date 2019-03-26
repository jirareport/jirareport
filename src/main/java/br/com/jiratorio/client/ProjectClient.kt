package br.com.jiratorio.client

import br.com.jiratorio.client.config.JiraClientConfiguration
import br.com.jiratorio.domain.jira.BoardStatusList
import br.com.jiratorio.domain.jira.JiraProject
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "project-client",
    url = "\${jira.url}",
    configuration = [
        JiraClientConfiguration::class
    ]
)
interface ProjectClient {

    @GetMapping("/rest/api/2/project")
    fun findAll(): List<JiraProject>

    @GetMapping("/rest/api/2/project/{projectId}/statuses")
    fun findStatusFromProject(@PathVariable("projectId") projectId: Long): List<BoardStatusList>

    @GetMapping("/rest/api/2/project/{projectId}")
    fun findById(@PathVariable("projectId") projectId: Long): JiraProject
}
