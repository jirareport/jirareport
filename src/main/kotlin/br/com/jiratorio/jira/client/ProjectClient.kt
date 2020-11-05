package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.external.ExternalBoard
import br.com.jiratorio.jira.client.config.JiraClientConfiguration
import br.com.jiratorio.jira.domain.JiraStatusList
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
    fun findAll(): List<ExternalBoard>

    @GetMapping("/rest/api/2/project/{projectId}/statuses")
    fun findStatusFromProject(@PathVariable("projectId") projectId: Long): List<JiraStatusList>

    @GetMapping("/rest/api/2/project/{projectId}")
    fun findById(@PathVariable("projectId") projectId: Long): Optional<ExternalBoard>

}
