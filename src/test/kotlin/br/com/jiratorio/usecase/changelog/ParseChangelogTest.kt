package br.com.jiratorio.usecase.changelog

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.usecase.parse.ParseChangelog
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class ParseChangelogTest {

    private val parseChangelog = ParseChangelog()

    @Test
    fun `parse changelog`() {
        val jiraChangelog = listOf(
            JiraChangelogItem(field = "issueType"),
            JiraChangelogItem(field = "other"),
            JiraChangelogItem(
                field = "status",
                fromString = "TODO",
                toString = "DEV",
                created = "05/01/2019".toLocalDateTime()
            ),
            JiraChangelogItem(field = "flag"),
            JiraChangelogItem(
                field = "status",
                fromString = "DEV",
                toString = "TEST",
                created = "10/01/2019".toLocalDateTime()
            ),
            JiraChangelogItem(field = "priority"),
            JiraChangelogItem(
                field = "status",
                fromString = "TEST",
                toString = "DONE",
                created = "15/01/2019".toLocalDateTime()
            ),
            JiraChangelogItem(field = "dueDate")
        )

        val issueCreationDate = "01/01/2019".toLocalDateTime()
        Assertions.assertThat(parseChangelog.execute(jiraChangelog, issueCreationDate, emptyList(), true))
            .contains(
                Changelog(
                    from = null,
                    to = "TODO",
                    created = "01/01/2019".toLocalDateTime(),
                    leadTime = 5,
                    endDate = "05/01/2019".toLocalDateTime()
                ),
                Changelog(
                    from = "TODO",
                    to = "DEV",
                    created = "05/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "10/01/2019".toLocalDateTime()
                ),
                Changelog(
                    from = "DEV",
                    to = "TEST",
                    created = "10/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "15/01/2019".toLocalDateTime()
                ),
                Changelog(
                    from = "TEST",
                    to = "DONE",
                    created = "15/01/2019".toLocalDateTime(),
                    leadTime = 0,
                    endDate = "15/01/2019".toLocalDateTime()
                )
            )
    }

}
