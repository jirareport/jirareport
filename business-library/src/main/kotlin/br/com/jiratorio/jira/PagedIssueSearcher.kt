package br.com.jiratorio.jira

import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import br.com.jiratorio.usecase.parse.IssueParser
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

class PagedIssueSearcher<T>(
    private val issueClient: IssueClient,
    private val issueParser: IssueParser<T>
) {

    fun search(jql: String, board: Board, holidays: List<LocalDate>): List<T> {
        val firstNode = issueClient.findByJql(
            filter = SearchJiraIssueRequest(
                jql = jql,
                startAt = 0,
                maxResults = PAGE_SIZE
            )
        )

        val total = firstNode["total"].intValue()
        val nodes: List<JsonNode> =
            if (total > PAGE_SIZE) {
                val totalPages = total / PAGE_SIZE
                buildNodes(
                    firstPage = firstNode,
                    otherPages = (1..totalPages)
                        .map { currentPage ->
                            val filter = SearchJiraIssueRequest(
                                jql = jql,
                                startAt = currentPage * PAGE_SIZE,
                                maxResults = PAGE_SIZE
                            )
                            issueClient.findByJql(filter)
                        }
                )
            } else {
                mutableListOf(firstNode)
            }

        return nodes.flatMap { issueParser.execute(it, board, holidays) }
    }

    private fun buildNodes(firstPage: JsonNode, otherPages: List<JsonNode>): List<JsonNode> =
        mutableListOf(firstPage)
            .also { it.addAll(otherPages) }

    companion object {
        const val PAGE_SIZE: Int = 50
    }
}
