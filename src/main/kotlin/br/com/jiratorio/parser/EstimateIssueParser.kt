package br.com.jiratorio.parser

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.jira.changelog.JiraChangelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.service.ChangelogService
import br.com.jiratorio.service.HolidayService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class EstimateIssueParser(
    private val holidayService: HolidayService,
    private val objectMapper: ObjectMapper,
    private val changelogService: ChangelogService,
    private val issueParserCoroutineDispatcher: ExecutorCoroutineDispatcher
) {

    @ExecutionTime
    fun parseEstimate(rawText: String, board: Board): List<EstimateIssue> {
        val holidays = holidayService.findDaysByBoard(board.id)

        val fluxColumn = FluxColumn(board)
        val startColumns = fluxColumn.startColumns

        val issues = objectMapper.readTree(rawText).path("issues")
        return runBlocking(issueParserCoroutineDispatcher) {
            issues
                .map {
                    async { parseIssue(it, board, startColumns, holidays) }
                }
                .mapNotNull {
                    it.await()
                }
        }
    }

    fun parseIssue(
        issue: JsonNode,
        board: Board,
        startColumns: Set<String>,
        holidays: List<LocalDate>
    ): EstimateIssue? {
        val fields = issue.path("fields")

        val changelogItems = extractChangelogItems(issue)
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
        val created = fields.get("created")
            .extractValueNotNull().fromJiraToLocalDateTime()

        var startDate: LocalDateTime? = null

        for (cl in changelog) {
            if (startDate == null && startColumns.contains(cl.to?.toUpperCase())) {
                startDate = cl.created
            }
        }

        if ("BACKLOG" == board.startColumn) {
            startDate = created
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

        val timeInImpediment = board.impedimentType?.timeInImpediment(
            board.impedimentColumns,
            changelogItems,
            changelog,
            LocalDateTime.now(),
            holidays,
            board.ignoreWeekend
        ) ?: 0

        val author: String? =
            if (fields.hasNonNull("creator")) {
                fields.path("creator").path("displayName").extractValue()
            } else {
                null
            }

        return EstimateIssue(
            creator = author,
            key = issue.get("key").extractValueNotNull(),
            issueType = fields.path("issuetype").extractValue(),
            created = created,
            startDate = startDate,
            leadTime = leadTime,
            system = fields.path(board.systemCF).extractValue(),
            epic = fields.path(board.epicCF).extractValue(),
            estimate = fields.path(board.estimateCF).extractValue(),
            project = fields.path(board.projectCF).extractValue(),
            summary = fields.get("summary").extractValueNotNull(),
            changelog = changelog,
            impedimentTime = timeInImpediment,
            priority = priority
        )
    }

    private fun extractChangelogItems(issue: JsonNode): List<JiraChangelogItem> {
        val changelog = objectMapper.treeToValue(issue.path("changelog"), JiraChangelog::class.java)
        changelog.histories.forEach { cl -> cl.items.forEach { i -> i.created = cl.created } }
        return changelog.histories.map { it.items }.flatten()
    }

}
