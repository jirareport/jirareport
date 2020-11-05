package br.com.jiratorio.jira.parser

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.JiraIssue
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.parallelStream
import br.com.jiratorio.extension.time.daysDiff
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.streams.toList

@Component
class JiraIssueParser(
    private val changelogParser: ChangelogParser,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun parse(root: JsonNode, board: BoardEntity, holidays: List<LocalDate>, parseUnfinishedIssue: Boolean): List<JiraIssue> {
        log.info("Action=parseIssue, root={}, board={}", root, board)

        val fluxColumn = FluxColumn(board)
        return root.path("issues")
            .parallelStream()
            .map { jsonNodeToIssue(it, board, holidays, fluxColumn, parseUnfinishedIssue) }
            .toList()
            .filterNotNull()
    }

    private fun jsonNodeToIssue(
        jsonNode: JsonNode,
        board: BoardEntity,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn,
        parseUnfinishedIssue: Boolean
    ): JiraIssue? {
        return try {
            parseIssue(jsonNode, board, holidays, fluxColumn, parseUnfinishedIssue)
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
        board: BoardEntity,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn,
        parseUnfinishedIssue: Boolean
    ): JiraIssue? {
        log.info("Method=parseIssue, Info=parsing, key={}", issue.path("key").extractValue())

        val fields = issue.path("fields")

        val created = fields.path("created")
            .extractValueNotNull().fromJiraToLocalDateTime()

        val parsedChangelog = changelogParser.parse(issue, created, holidays, board.ignoreWeekend, parseUnfinishedIssue)

        val startAndEndDate = fluxColumn.calcStartAndEndDate(parsedChangelog.columnChangelog, created)
        val startDate = startAndEndDate.first
        val endDate = if (parseUnfinishedIssue && startAndEndDate.second == null)
            LocalDateTime.now()
        else
            startAndEndDate.second

        if (startDate == null || endDate == null) {
            return null
        }

        val author: String? =
            if (fields.hasNonNull("creator")) {
                fields.path("creator").path("displayName").extractValue()
            } else {
                null
            }

        val priority: String? =
            if (fields.hasNonNull("priority")) {
                fields.path("priority").extractValue()
            } else {
                null
            }

        val dynamicFields = board.dynamicFields
            ?.map { it.name to fields.path(it.field).extractValue() }
            ?.toMap()
            ?: emptyMap()

        var issueType: String? = null
        if (fields.hasNonNull("issuetype") && fields.path("issuetype").isObject) {
            issueType = fields.path("issuetype").extractValue()
        }

        val leadTime = startDate.daysDiff(
            endDate,
            holidays,
            board.ignoreWeekend
        )

        return JiraIssue(
            key = issue.path("key").extractValueNotNull(),
            issueType = issueType,
            leadTime = leadTime,
            creator = author,
            created = created,
            startDate = startDate,
            endDate = endDate,
            system = fields.path(board.systemCF).extractValue(),
            epic = fields.path(board.epicCF).extractValue(),
            estimate = fields.path(board.estimateCF).extractValue(),
            project = fields.path(board.projectCF).extractValue(),
            summary = fields.path("summary").extractValueNotNull(),
            priority = priority,
            dynamicFields = dynamicFields,
            changelog = parsedChangelog
        )
    }

}
