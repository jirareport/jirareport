package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.util.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class JQLServiceImpl : JQLService {

    val log = logger()

    override fun finalizedIssues(board: Board, start: LocalDate, end: LocalDate): String {
        log.info("Method=finalizedIssues, board={}, startDate={}, endDate={}", board, start, end)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = start.format(formatter)
        val endDate = end.format(formatter) + " 23:59"
        val endColumn = board.endColumn
        val project = board.externalId

        val fluxColumn = FluxColumn(board)
        val lastColumn = fluxColumn.lastColumn
        val startColumns = fluxColumn.startColumns.joinToString(",") { "'$it'" }
        val endColumns = fluxColumn.endColumns.joinToString(",") { "'$it'" }

        val ignoredIssueTypes = board.ignoreIssueType?.joinToString(",", "AND issueType NOT IN (", ")") {
            "'$it'"
        } ?: ""

        return """
              | project = '$project'
              | AND (
              |      STATUS CHANGED TO '$endColumn' DURING('$startDate', '$endDate')
              |      OR (
              |          STATUS CHANGED TO '$lastColumn' DURING ('$startDate', '$endDate')
              |          AND NOT STATUS CHANGED TO '$endColumn'
              |      )
              |      OR (
              |          resolutiondate >= '$startDate' AND resolutiondate <= '$endDate'
              |          AND NOT STATUS CHANGED TO '$lastColumn'
              |          AND NOT STATUS CHANGED TO '$endColumn'
              |      )
              | )
              | $ignoredIssueTypes
              | AND status WAS IN ($startColumns)
              | AND status IN ($endColumns)
        """.trimMargin()
    }

    override fun openedIssues(board: Board): String {
        log.info("Method=openedIssues, board={}", board)

        val wipColumns = FluxColumn(board).wipColumns.joinToString(",") { "'$it'" }
        val project = board.externalId

        val ignoredIssueTypes = board.ignoreIssueType?.joinToString(",", "AND issueType NOT IN (", ")") {
            "'$it'"
        } ?: ""

        return """
            project = '$project'
            AND status IN ($wipColumns)
            $ignoredIssueTypes
        """.trimMargin()
    }

}
