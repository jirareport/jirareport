package br.com.jiratorio.factory

import br.com.jiratorio.dsl.jira.JiraIssue
import br.com.jiratorio.dsl.jira.JiraIssuePeriod
import br.com.jiratorio.dsl.wireMock
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.stereotype.Component

@Component
class JiraIssueFactory(val objectMapper: ObjectMapper) {

    fun createIssue(issuesBlock: () -> Array<JiraIssue>) {
        wireMock {
            get("/jira/rest/api/2/search") {
                withQueryParam("expand", WireMock.equalTo("changelog"))
                withQueryParam("maxResults", WireMock.equalTo("100"))
                withQueryParam("jql", WireMock.matching("(.*)"))
            }

            willReturn {
                withHeader("Content-Type", "application/json")
                withStatus(200)
                withBody(objectMapper.writeValueAsString(JiraIssuePeriod(issuesBlock())))
            }
        }
    }

}
