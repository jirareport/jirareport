package br.com.jiratorio.usecase.parse

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimatedIssue
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.extension.containsUpperCase
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.parallelStream
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.usecase.changelog.ParseChangelog
import br.com.jiratorio.usecase.holiday.FindHolidayDays
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.streams.toList

@Component
class ParseEstimateIssue(
    private val parseJiraChangelog: ParseJiraChangelog,
    private val parserChangelog: ParseChangelog,
    private val findHolidayDays: FindHolidayDays
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(root: JsonNode, board: Board): List<EstimatedIssue> {
        val holidays = findHolidayDays.execute(board.id)

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

        val changelogItems = parseJiraChangelog.execute(issue)
        val changelog = parserChangelog.execute(changelogItems, created, holidays, board.ignoreWeekend)

        if (changelog.isNotEmpty()) {
            val changelogItem = changelog.last()
            changelogItem.leadTime = changelogItem.created.daysDiff(
                LocalDateTime.now(),
                holidays,
                board.ignoreWeekend
            )
            changelogItem.endDate = LocalDateTime.now()
        }

        var startDate: LocalDateTime? = null

        for (cl in changelog) {
            if (startDate == null && startColumns.containsUpperCase(cl.to)) {
                startDate = cl.created
            }
        }

        if ("BACKLOG" == board.startColumn) {
            startDate = fields.get("created")
                .extractValueNotNull().fromJiraToLocalDateTime()
        }
        if (startDate == null) {
            return null
        }

        val priority: String? =
            if (fields.hasNonNull("priority")) {
                fields.path("priority").extractValue()
            } else {
                null
            }

        val leadTime = startDate.daysDiff(LocalDateTime.now(), holidays, board.ignoreWeekend)

        val impedimentCalculatorResult = board.impedimentType?.calcImpediment(
            board.impedimentColumns,
            changelogItems,
            changelog,
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
            changelog = changelog,
            priority = priority,
            impedimentTime = impedimentCalculatorResult.timeInImpediment,
            impedimentHistory = impedimentCalculatorResult.impedimentHistory
        )
    }

}
