package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.response.FieldResponse
import br.com.jiratorio.jira.client.config.JiraClientConfiguration
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
    fun findAll(): List<FieldResponse>

}
