package br.com.jiratorio.client

import br.com.jiratorio.config.JiraClientConfiguration
import br.com.jiratorio.jira.BoardStatusList
import br.com.jiratorio.jira.JiraProjectDetails
import br.com.jiratorio.jira.JiraProject
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.Optional

@FeignClient(
    name = "project-client",
    url = "\${jira.url}",
    configuration = [
        JiraClientConfiguration::class
    ],
    decode404 = true
)
interface ProjectClient {

    @GetMapping("/rest/api/2/project")
    fun findAll(): List<JiraProject>

    @GetMapping("/rest/api/2/project/{projectId}/statuses")
    fun findStatusFromProject(@PathVariable("projectId") projectId: Long): List<BoardStatusList>

    @GetMapping("/rest/api/2/project/{projectId}")
    fun findById(@PathVariable("projectId") projectId: Long): Optional<JiraProjectDetails>

}
