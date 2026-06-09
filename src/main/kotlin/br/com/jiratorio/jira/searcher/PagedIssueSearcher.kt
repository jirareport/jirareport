package br.com.jiratorio.jira.searcher

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.JiraIssue
import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import br.com.jiratorio.jira.client.IssueClient
import br.com.jiratorio.jira.parser.JiraIssueParser
import tools.jackson.databind.JsonNode
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
        val nodes = mutableListOf<JsonNode>()
        var nextPageToken: String? = null
        do {
            val node = issueClient.findByJql(
                filter = SearchJiraIssueRequest(
                    jql = jql,
                    maxResults = PAGE_SIZE,
                    nextPageToken = nextPageToken,
                )
            )
            nodes.add(node)
            nextPageToken = node.path("nextPageToken").asText(null)
        } while (nextPageToken != null)

        return nodes.flatMap { issueParser.parse(it, board, holidays, parseUnfinishedIssue) }
    }

    companion object {
        const val PAGE_SIZE: Int = 50
    }

}
