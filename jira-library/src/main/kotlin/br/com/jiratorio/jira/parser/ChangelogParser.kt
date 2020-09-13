package br.com.jiratorio.jira.parser

import br.com.jiratorio.domain.changelog.Changelog
import br.com.jiratorio.extension.extractValue
import br.com.jiratorio.jira.domain.JiraChangelog
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ChangelogParser(
    private val fieldChangelogParser: FieldChangelogParser,
    private val columnChangelogParser: ColumnChangelogParser,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun parse(
        issue: JsonNode,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?,
        endsToday: Boolean = false,
    ): Changelog {
        val jiraChangelog: List<JiraChangelog> = parseJiraChangelog(issue)

        val fieldChangelog = fieldChangelogParser.parse(jiraChangelog)
        val columnChangelog = columnChangelogParser.parse(jiraChangelog, issueCreationDate, holidays, ignoreWeekend, endsToday)

        return Changelog(fieldChangelog, columnChangelog)
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
