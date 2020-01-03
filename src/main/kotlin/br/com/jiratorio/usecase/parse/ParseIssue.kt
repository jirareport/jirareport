package br.com.jiratorio.usecase.parse

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.parallelStream
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.usecase.duedate.CreateDueDateHistory
import br.com.jiratorio.usecase.efficiency.CalculateEfficiency
import br.com.jiratorio.usecase.holiday.FindHolidayDays
import br.com.jiratorio.usecase.parse.changelog.ParseChangelog
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.streams.toList

@Component
class ParseIssue(
    private val createDueDateHistory: CreateDueDateHistory,
    private val parseChangelog: ParseChangelog,
    private val calculateEfficiency: CalculateEfficiency,
    private val findHolidayDays: FindHolidayDays
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(root: JsonNode, board: Board): List<Issue> {
        log.info("Action=parseIssue, root={}, board={}", root, board)

        val holidays = findHolidayDays.execute(board.id)

        val fluxColumn = FluxColumn(board)
        return root.path("issues")
            .parallelStream()
            .map { jsonNodeToIssue(it, board, holidays, fluxColumn) }
            .toList()
            .filterNotNull()
    }

    private fun jsonNodeToIssue(
        jsonNode: JsonNode,
        board: Board,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn
    ): Issue? {
        return try {
            parseIssue(jsonNode, board, holidays, fluxColumn)
        } catch (e: Exception) {
            log.error(
                "Method=jsonNodeToIssue, info=Error parsing issue, issue={}, err={}",
                jsonNode.path("key").extractValue(), e.message
            )
            throw e
        }
    }

    private fun parseIssue(
        issue: JsonNode,
        board: Board,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn
    ): Issue? {
        log.info("Method=parseIssue, Info=parsing, key={}", issue.path("key").extractValue())

        val fields = issue.path("fields")

        val created = fields.path("created")
            .extractValueNotNull().fromJiraToLocalDateTime()

        val parsedChangelog = parseChangelog.execute(issue, created, holidays, board.ignoreWeekend)

        val (
            startDate,
            endDate
        ) = fluxColumn.calcStartAndEndDate(parsedChangelog.columnChangelog, created)

        if (startDate == null || endDate == null) {
            return null
        }

        val leadTime = startDate.daysDiff(endDate, holidays, board.ignoreWeekend)

        val author: String? =
            if (fields.hasNonNull("creator")) {
                fields.path("creator").path("displayName").extractValue()
            } else {
                null
            }

        var deviationOfEstimate: Long? = null
        var dueDateHistory: List<DueDateHistory>? = null

        val dueDateType = board.dueDateType
        val dueDateCF = board.dueDateCF
        if (dueDateCF != null && dueDateCF.isNotEmpty() && dueDateType != null) {
            dueDateHistory = createDueDateHistory.execute(dueDateCF, parsedChangelog.fieldChangelog)
            deviationOfEstimate =
                dueDateType.calcDeviationOfEstimate(dueDateHistory, endDate, board.ignoreWeekend, holidays)
        }

        val impedimentCalculatorResult: ImpedimentCalculatorResult = board.impedimentType?.calcImpediment(
            board.impedimentColumns,
            parsedChangelog,
            endDate,
            holidays,
            board.ignoreWeekend
        ) ?: ImpedimentCalculatorResult()

        val priority: String? =
            if (fields.hasNonNull("priority")) {
                fields.path("priority").extractValue()
            } else {
                null
            }

        val dynamicFields = board.dynamicFields?.map {
            it.name to fields.path(it.field).extractValue()
        }?.toMap()

        val efficiency = calculateEfficiency.execute(
            columnChangelog = parsedChangelog.columnChangelog,
            touchingColumns = board.touchingColumns,
            waitingColumns = board.waitingColumns,
            holidays = holidays,
            ignoreWeekend = board.ignoreWeekend
        )

        var issueType: String? = null
        if (fields.hasNonNull("issuetype") && fields.path("issuetype").isObject) {
            issueType = fields.path("issuetype").extractValue()
        }

        return Issue(
            key = issue.path("key").extractValueNotNull(),
            issueType = issueType,
            creator = author,
            created = created,
            startDate = startDate,
            endDate = endDate,
            leadTime = leadTime,
            system = fields.path(board.systemCF).extractValue(),
            epic = fields.path(board.epicCF).extractValue(),
            estimate = fields.path(board.estimateCF).extractValue(),
            project = fields.path(board.projectCF).extractValue(),
            summary = fields.path("summary").extractValueNotNull(),
            columnChangelog = parsedChangelog.columnChangelog,
            board = board,
            deviationOfEstimate = deviationOfEstimate,
            dueDateHistory = dueDateHistory,
            impedimentTime = impedimentCalculatorResult.timeInImpediment,
            impedimentHistory = impedimentCalculatorResult.impedimentHistory,
            priority = priority,
            dynamicFields = dynamicFields,
            waitTime = efficiency.waitTime,
            touchTime = efficiency.touchTime,
            pctEfficiency = efficiency.pctEfficiency
        )
    }

}
