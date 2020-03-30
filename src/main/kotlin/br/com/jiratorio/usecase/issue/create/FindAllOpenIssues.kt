package br.com.jiratorio.usecase.issue.create

import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.parsed.ParsedIssue
import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import br.com.jiratorio.usecase.jql.CreateFinalizedIssueJql
import br.com.jiratorio.usecase.parse.ParseIssue
import java.time.LocalDate

@UseCase
class FindAllOpenIssues(
    private val createFinalizedIssueJql: CreateFinalizedIssueJql,
    private val parseIssue: ParseIssue,
    private val issueClient: IssueClient
) {

    fun execute(board: Board, holidays: List<LocalDate>, startDate: LocalDate, endDate: LocalDate): Pair<String, List<ParsedIssue>> {
        val jql = createFinalizedIssueJql.execute(board, startDate, endDate)

        val root = issueClient.findByJql(
            filter = SearchJiraIssueRequest(
                jql = jql
            )
        )

        return Pair(jql, parseIssue.execute(root, board, holidays))
    }

}
