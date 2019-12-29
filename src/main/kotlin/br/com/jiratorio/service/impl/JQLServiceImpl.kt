package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.usecase.jql.FinalizedIssueJql
import br.com.jiratorio.usecase.jql.OpenedIssueJql
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class JQLServiceImpl(
    private val openedIssueJql: OpenedIssueJql,
    private val finalizedIssueJql: FinalizedIssueJql
) : JQLService {

    override fun finalizedIssues(board: Board, start: LocalDate, end: LocalDate): String =
        finalizedIssueJql.execute(board, start, end)

    override fun openedIssues(board: Board): String =
        openedIssueJql.execute(board)

}
