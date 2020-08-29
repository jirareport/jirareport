package br.com.jiratorio.client

import br.com.jiratorio.client.config.JiraClientConfiguration
import br.com.jiratorio.domain.jira.JiraField
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "field-client",
    url = "\${jira.url}",
    configuration = [
        JiraClientConfiguration::class
    ]
)
interface FieldClient {

    @GetMapping("/rest/api/2/field")
    fun findAll(): List<JiraField>

}
