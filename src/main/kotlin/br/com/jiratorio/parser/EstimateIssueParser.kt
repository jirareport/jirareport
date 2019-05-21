package br.com.jiratorio.parser

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.service.ChangelogService
import br.com.jiratorio.service.HolidayService
import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Flowable
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class EstimateIssueParser(
    private val holidayService: HolidayService,
    private val changelogService: ChangelogService,
    private val changelogParser: ChangelogParser
) {

    @ExecutionTime
    fun parseEstimate(root: JsonNode, board: Board): List<EstimateIssue> {
        val holidays = holidayService.findDaysByBoard(board.id)

        val fluxColumn = FluxColumn(board)
        val startColumns = fluxColumn.startColumns

        return Flowable.fromIterable(root.path("issues"))
            .parallel(10)
            .map { parseIssue(it, board, startColumns, holidays) }
            .sequential()
            .blockingIterable()
            .filterNotNull()
    }

    fun parseIssue(
        issue: JsonNode,
        board: Board,
        startColumns: Set<String>,
        holidays: List<LocalDate>
    ): EstimateIssue? {
        val fields = issue.path("fields")

        val changelogItems = changelogParser.extractChangelogItems(issue)
        val changelog = changelogService.parseChangelog(changelogItems, holidays, board.ignoreWeekend)
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
            if (startDate == null && startColumns.contains(cl.to?.toUpperCase())) {
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

        return EstimateIssue(
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
