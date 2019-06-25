package br.com.jiratorio.parser

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.service.ChangelogService
import br.com.jiratorio.service.DueDateService
import br.com.jiratorio.service.EfficiencyService
import br.com.jiratorio.service.HolidayService
import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Flowable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class IssueParser(
    private val holidayService: HolidayService,
    private val dueDateService: DueDateService,
    private val changelogService: ChangelogService,
    private val efficiencyService: EfficiencyService,
    private val changelogParser: ChangelogParser
) {

    @ExecutionTime
    @Transactional(readOnly = true)
    fun parse(root: JsonNode, board: Board): List<Issue> {
        val holidays = holidayService.findDaysByBoard(board.id)

        val fluxColumn = FluxColumn(board)
        return Flowable.fromIterable(root.path("issues"))
            .parallel(10)
            .map {
                try {
                    parseIssue(it, board, holidays, fluxColumn)
                } catch (e: Exception) {
                    log.error(
                        "Method=parse, info=Error parsing issue, issue={}, err={}",
                        it.path("key").extractValue(), e.message
                    )
                    throw e
                }
            }
            .sequential()
            .blockingIterable()
            .filterNotNull()
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

        val changelogItems = changelogParser.extractChangelogItems(issue)
        val changelog = changelogService.parseChangelog(changelogItems, created, holidays, board.ignoreWeekend)

        val (
            startDate,
            endDate
        ) = fluxColumn.calcStartAndEndDate(changelog, created)

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
            dueDateHistory = dueDateService.extractDueDateHistory(dueDateCF, changelogItems)
            deviationOfEstimate =
                dueDateType.calcDeviationOfEstimate(dueDateHistory, endDate, board.ignoreWeekend, holidays)
        }

        val impedimentCalculatorResult: ImpedimentCalculatorResult = board.impedimentType?.calcImpediment(
            board.impedimentColumns,
            changelogItems,
            changelog,
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

        val efficiency = efficiencyService.calcEfficiency(
            changelog,
            board.touchingColumns,
            board.waitingColumns,
            holidays,
            board.ignoreWeekend
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
            changelog = changelog,
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
