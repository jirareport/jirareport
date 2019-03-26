package br.com.jiratorio.client

import br.com.jiratorio.client.config.JiraClientConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "issue-client",
    url = "\${jira.url}",
    configuration = [
        JiraClientConfiguration::class
    ]
)
interface IssueClient {

    @GetMapping("/rest/api/2/search?expand=changelog&maxResults=100")
    fun findByJql(@RequestParam("jql") jql: String): String

}
