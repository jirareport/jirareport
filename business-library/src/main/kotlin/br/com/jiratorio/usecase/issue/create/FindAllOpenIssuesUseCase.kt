package br.com.jiratorio.usecase.issue.create

import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.parsed.ParsedIssue
import br.com.jiratorio.jira.PagedIssueSearcher
import br.com.jiratorio.usecase.jql.CreateFinalizedIssueJqlUseCase
import br.com.jiratorio.usecase.parse.ParseIssueUseCase
import java.time.LocalDate

@UseCase
class FindAllOpenIssuesUseCase(
    private val createFinalizedIssueJql: CreateFinalizedIssueJqlUseCase,
    private val parseIssue: ParseIssueUseCase,
    private val issueClient: IssueClient
) {

    fun execute(board: BoardEntity, holidays: List<LocalDate>, startDate: LocalDate, endDate: LocalDate): Pair<String, List<ParsedIssue>> {
        val jql = createFinalizedIssueJql.execute(board, startDate, endDate)

        val searcher = PagedIssueSearcher(
            issueClient = issueClient,
            issueParser = parseIssue
        )

        return Pair(
            first = jql,
            second = searcher.search(jql, board, holidays)
        )
    }

}
