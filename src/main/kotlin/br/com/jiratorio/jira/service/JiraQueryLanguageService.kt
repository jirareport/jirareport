package br.com.jiratorio.jira.service

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.extension.EMPTY
import br.com.jiratorio.extension.sanitizeJql
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class JiraQueryLanguageService {

    fun buildFinalizedIssueQuery(board: BoardEntity, start: LocalDate, end: LocalDate): String {
        val startDate = start.format(DATE_TIME_FORMATTER)
        val endDate = "${end.format(DATE_TIME_FORMATTER)} 23:59"
        val endColumn = board.endColumn
        val project = board.externalId

        val fluxColumn = FluxColumn(board)
        val lastColumn = fluxColumn.lastColumn
        val startColumns = fluxColumn.startColumns.commaSeparated()
        val endColumns = fluxColumn.endColumns.commaSeparated()

        val ignoredIssueTypes = buildIgnoredIssueTypes(board)

        val additionalFilter: String = buildAdditionalFilter(board)

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
              | $additionalFilter
        """
            .sanitizeJql()
    }

    fun buildOpenedIssueQuery(board: BoardEntity): String {
        val fluxColumn = FluxColumn(board)
        val wipColumns = fluxColumn.wipColumns.commaSeparated()

        val project = board.externalId

        val ignoredIssueTypes = buildIgnoredIssueTypes(board)

        val additionalFilter: String = buildAdditionalFilter(board)

        return """
            | project = '$project'
            | AND status IN ($wipColumns)
            | $ignoredIssueTypes
            | $additionalFilter
        """.sanitizeJql()
    }

    private fun Collection<*>.commaSeparated(): String =
        joinToString(",") { "'$it'" }

    private fun buildIgnoredIssueTypes(board: BoardEntity) =
        board.ignoreIssueType
            ?.let { str -> str.joinToString(",", "AND issueType NOT IN (", ")") { "'$it'" } }
            ?: String.EMPTY

    private fun buildAdditionalFilter(board: BoardEntity) =
        board.additionalFilter
            ?.let { "AND ($it)" }
            ?: String.EMPTY

    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

}
