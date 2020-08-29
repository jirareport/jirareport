package br.com.jiratorio.usecase.parse

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.domain.estimate.EstimatedIssue
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.extension.containsUpperCase
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.parallelStream
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.usecase.parse.changelog.ParseChangelog
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.streams.toList

@UseCase
class ParseEstimateIssue(
    private val parseChangelog: ParseChangelog
) : IssueParser<EstimatedIssue> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(root: JsonNode, board: Board, holidays: List<LocalDate>): List<EstimatedIssue> {
        log.info("Action=parseEstimateIssue, root={}, board={}", root, board)

        val fluxColumn = FluxColumn(board)
        val startColumns = fluxColumn.startColumns

        return root.path("issues")
            .parallelStream()
            .map { jsonNodeToEstimateIssue(it, board, startColumns, holidays) }
            .toList()
            .filterNotNull()
    }

    private fun jsonNodeToEstimateIssue(
        jsonNode: JsonNode,
        board: Board,
        startColumns: Set<String>,
        holidays: List<LocalDate>
    ): EstimatedIssue? =
        try {
            parseIssue(jsonNode, board, startColumns, holidays)
        } catch (e: Exception) {
            log.error(
                "Method=jsonNodeToIssue, info=Error parsing estimate Issue, issue={}, err={}",
                jsonNode.path("key").extractValue(), e.message
            )
            throw e
        }

    private fun parseIssue(
        issue: JsonNode,
        board: Board,
        startColumns: Set<String>,
        holidays: List<LocalDate>
    ): EstimatedIssue? {
        val fields = issue.path("fields")

        val created = fields.path("created")
            .extractValueNotNull().fromJiraToLocalDateTime()

        val parsedChangelog = parseChangelog.execute(issue, created, holidays, board.ignoreWeekend)

        val startDate = extractStartDate(
            startColumns,
            created,
            parsedChangelog.columnChangelog,
            board,
            holidays
        ) ?: return null

        val priority: String? =
            if (fields.hasNonNull("priority")) {
                fields.path("priority").extractValue()
            } else {
                null
            }

        val leadTime = startDate.daysDiff(LocalDateTime.now(), holidays, board.ignoreWeekend)

        val impedimentCalculatorResult = board.impedimentType?.calcImpediment(
            board.impedimentColumns,
            parsedChangelog,
            LocalDateTime.now(),
            holidays,
            board.ignoreWeekend
        ) ?: ImpedimentCalculatorResult()

        val author: String? =
            if (fields.hasNonNull("creator")) {
                fields.path("creator").path("displayName").extractValue()
            } else {
                null
            }

        return EstimatedIssue(
            creator = author,
            key = issue.path("key").extractValueNotNull(),
            issueType = fields.path("issuetype").extractValue(),
            startDate = startDate,
            leadTime = leadTime,
            system = fields.path(board.systemCF).extractValue(),
            epic = fields.path(board.epicCF).extractValue(),
            estimate = fields.path(board.estimateCF).extractValue(),
            project = fields.path(board.projectCF).extractValue(),
            summary = fields.path("summary").extractValueNotNull(),
            columnChangelog = parsedChangelog.columnChangelog,
            priority = priority,
            impedimentTime = impedimentCalculatorResult.timeInImpediment,
            impedimentHistory = impedimentCalculatorResult.impedimentHistory
        )
    }

    private fun extractStartDate(
        startColumns: Set<String>,
        created: LocalDateTime,
        columnChangelog: Set<ColumnChangelog>,
        board: Board,
        holidays: List<LocalDate>
    ): LocalDateTime? {
        if (columnChangelog.isNotEmpty()) {
            val changelogItem = columnChangelog.last()
            changelogItem.leadTime = changelogItem.startDate.daysDiff(
                LocalDateTime.now(),
                holidays,
                board.ignoreWeekend
            )
            changelogItem.endDate = LocalDateTime.now()
        }

        var startDate: LocalDateTime? = null

        for (cl in columnChangelog) {
            if (startDate == null && startColumns.containsUpperCase(cl.to)) {
                startDate = cl.startDate
            }
        }

        return if ("BACKLOG" == board.startColumn)
            created
        else
            startDate
    }

}
