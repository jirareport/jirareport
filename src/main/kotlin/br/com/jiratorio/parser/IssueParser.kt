package br.com.jiratorio.parser

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.jira.changelog.JiraChangelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
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
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.Executors

@Component
class IssueParser(
    private val holidayService: HolidayService,
    private val objectMapper: ObjectMapper,
    private val dueDateService: DueDateService,
    private val changelogService: ChangelogService,
    private val efficiencyService: EfficiencyService,
    private val dispatcher: ExecutorCoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
) {

    @ExecutionTime
    @Transactional(readOnly = true)
    fun parse(rawText: String, board: Board): List<Issue> {
        val holidays = holidayService.findDaysByBoard(board.id)

        val fluxColumn = FluxColumn(board)
        val issues = objectMapper.readTree(rawText).path("issues")

        return runBlocking {
            issues.map {
                try {
                    async(dispatcher) { parseIssue(it, board, holidays, fluxColumn) }
                } catch (e: Exception) {
                    log.error(
                        "Method=parse, info=Error parsing issue, issue={}, err={}",
                        it.path("key").extractValue(), e.message
                    )
                    throw e
                }
            }.mapNotNull {
                val issue = it.await()
                log.info("Method=parseIssue, Info=parsed, key={}", issue?.key)
                issue
            }
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

        val changelogItems = extractChangelogItems(issue)
        val changelog = changelogService.parseChangelog(changelogItems, holidays, board.ignoreWeekend)

        val created = fields.path("created")
            .extractValueNotNull().fromJiraToLocalDateTime()

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

        val timeInImpediment = board.impedimentType?.timeInImpediment(
            board.impedimentColumns,
            changelogItems,
            changelog,
            endDate,
            holidays,
            board.ignoreWeekend
        ) ?: 0L

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
            impedimentTime = timeInImpediment,
            priority = priority,
            dynamicFields = dynamicFields,
            waitTime = efficiency.waitTime,
            touchTime = efficiency.touchTime,
            pctEfficiency = efficiency.pctEfficiency
        )
    }

    private fun extractChangelogItems(issue: JsonNode): List<JiraChangelogItem> {
        val changelog = objectMapper.treeToValue(issue.path("changelog"), JiraChangelog::class.java)
        changelog.histories.forEach { cl -> cl.items.forEach { i -> i.created = cl.created } }
        return changelog.histories.map { it.items }.flatten()
    }

}
