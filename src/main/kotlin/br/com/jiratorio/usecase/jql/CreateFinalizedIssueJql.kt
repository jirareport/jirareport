package br.com.jiratorio.usecase.jql

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@UseCase
class CreateFinalizedIssueJql {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(board: Board, start: LocalDate, end: LocalDate): String {
        log.info("Action=createFinalizedIssueJql, board={}, startDate={}, endDate={}", board, start, end)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = start.format(formatter)
        val endDate = end.format(formatter) + " 23:59"
        val endColumn = board.endColumn
        val project = board.externalId

        val fluxColumn = FluxColumn(board)
        val lastColumn = fluxColumn.lastColumn
        val startColumns = fluxColumn.startColumns.joinToString(",") { "'$it'" }
        val endColumns = fluxColumn.endColumns.joinToString(",") { "'$it'" }

        val ignoredIssueTypes = board.ignoreIssueType.let {
            if (it.isNullOrEmpty()) {
                ""
            } else {
                it.joinToString(",", "AND issueType NOT IN (", ")") { "'$it'" }
            }
        }

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
        """
            .trimMargin()
            .replace("\n", "")
            .replace("\\", "\\\\")
    }

}
