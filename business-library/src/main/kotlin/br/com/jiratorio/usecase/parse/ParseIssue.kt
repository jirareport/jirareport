package br.com.jiratorio.usecase.parse

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.parsed.ParsedIssue
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.extension.extractValueNotNull
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.parallelStream
import br.com.jiratorio.usecase.parse.changelog.ParseChangelog
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.streams.toList

@UseCase
class ParseIssue(
    private val parseChangelog: ParseChangelog
) : IssueParser<ParsedIssue> {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun execute(root: JsonNode, board: BoardEntity, holidays: List<LocalDate>): List<ParsedIssue> {
        log.info("Action=parseIssue, root={}, board={}", root, board)

        val fluxColumn = FluxColumn(board)
        return root.path("issues")
            .parallelStream()
            .map { jsonNodeToIssue(it, board, holidays, fluxColumn) }
            .toList()
            .filterNotNull()
    }

    private fun jsonNodeToIssue(
        jsonNode: JsonNode,
        board: BoardEntity,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn
    ): ParsedIssue? {
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
        board: BoardEntity,
        holidays: List<LocalDate>,
        fluxColumn: FluxColumn
    ): ParsedIssue? {
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

        val dynamicFields = board.dynamicFields?.map {
            it.name to fields.path(it.field).extractValue()
        }?.toMap()

        var issueType: String? = null
        if (fields.hasNonNull("issuetype") && fields.path("issuetype").isObject) {
            issueType = fields.path("issuetype").extractValue()
        }

        return ParsedIssue(
            key = issue.path("key").extractValueNotNull(),
            issueType = issueType,
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
            parsedChangelog = parsedChangelog
        )
    }

}
