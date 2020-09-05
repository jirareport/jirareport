package br.com.jiratorio.usecase.parse.changelog

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.parsed.ParsedChangelog
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.jira.JiraChangelog
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@UseCase
class ParseChangelogUseCase(
    private val parseFieldChangelog: ParseFieldChangelogUseCase,
    private val parseColumnChangelog: ParseColumnChangelogUseCase
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        issue: JsonNode,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ParsedChangelog {
        log.info("Action=parseChangelog, issue={}", issue)

        val jiraChangelog: List<JiraChangelog> = parseJiraChangelog(issue)

        val fieldChangelog = parseFieldChangelog.execute(jiraChangelog)
        val columnChangelog = parseColumnChangelog.execute(jiraChangelog, issueCreationDate, holidays, ignoreWeekend)

        return ParsedChangelog(
            fieldChangelog,
            columnChangelog
        )
    }

    private fun parseJiraChangelog(issue: JsonNode): List<JiraChangelog> =
        issue.path("changelog")
            .path("histories")
            .flatMap { history ->
                history.path("items")
                    .map { item ->
                        JiraChangelog(
                            field = item.path("field").extractValue(),
                            from = item.path("fromString").extractValue(),
                            to = item.path("toString").extractValue(),
                            created = LocalDateTime.parse(
                                history.path("created").extractValue(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            )
                        )
                    }
            }

}
