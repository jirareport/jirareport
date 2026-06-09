package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface IssueClient {

    @PostExchange("/rest/api/2/search")
    fun findByJql(@RequestBody filter: SearchJiraIssueRequest): JsonNode

}
