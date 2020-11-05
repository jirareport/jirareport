package br.com.jiratorio.jira.searcher

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.JiraIssue
import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import br.com.jiratorio.jira.client.IssueClient
import br.com.jiratorio.jira.parser.JiraIssueParser
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class PagedIssueSearcher(
    private val issueClient: IssueClient,
    private val issueParser: JiraIssueParser,
) {

    fun search(
        jql: String,
        board: BoardEntity,
        holidays: List<LocalDate>,
        parseUnfinishedIssue: Boolean = false,
    ): List<JiraIssue> {
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

        return nodes.flatMap { issueParser.parse(it, board, holidays, parseUnfinishedIssue) }
    }

    private fun buildNodes(firstPage: JsonNode, otherPages: List<JsonNode>): List<JsonNode> =
        mutableListOf(firstPage)
            .also { it.addAll(otherPages) }

    companion object {
        const val PAGE_SIZE: Int = 50
    }

}
