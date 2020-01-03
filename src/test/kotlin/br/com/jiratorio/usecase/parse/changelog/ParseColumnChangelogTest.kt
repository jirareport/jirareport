package br.com.jiratorio.usecase.parse.changelog

import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.domain.jira.JiraChangelog
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@Tag("unit")
internal class ParseColumnChangelogTest {

    private val parseColumnChangelog = ParseColumnChangelog()

    @Test
    fun `parse column changelog`() {
        val jiraChangelog = listOf(
            JiraChangelog(
                field = "issueType",
                created = LocalDateTime.now()
            ),
            JiraChangelog(
                field = "other",
                created = LocalDateTime.now()
            ),
            JiraChangelog(
                field = "status",
                from = "TODO",
                to = "DEV",
                created = "05/01/2019".toLocalDateTime()
            ),
            JiraChangelog(
                field = "flag",
                created = LocalDateTime.now()
            ),
            JiraChangelog(
                field = "status",
                from = "DEV",
                to = "TEST",
                created = "10/01/2019".toLocalDateTime()
            ),
            JiraChangelog(
                field = "priority",
                created = LocalDateTime.now()
            ),
            JiraChangelog(
                field = "status",
                from = "TEST",
                to = "DONE",
                created = "15/01/2019".toLocalDateTime()
            ),
            JiraChangelog(
                field = "dueDate",
                created = LocalDateTime.now()
            )
        )

        val issueCreationDate = "01/01/2019".toLocalDateTime()
        Assertions.assertThat(parseColumnChangelog.execute(jiraChangelog, issueCreationDate, emptyList(), true))
            .contains(
                ColumnChangelog(
                    from = null,
                    to = "TODO",
                    startDate = "01/01/2019".toLocalDateTime(),
                    leadTime = 5,
                    endDate = "05/01/2019".toLocalDateTime()
                ),
                ColumnChangelog(
                    from = "TODO",
                    to = "DEV",
                    startDate = "05/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "10/01/2019".toLocalDateTime()
                ),
                ColumnChangelog(
                    from = "DEV",
                    to = "TEST",
                    startDate = "10/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "15/01/2019".toLocalDateTime()
                ),
                ColumnChangelog(
                    from = "TEST",
                    to = "DONE",
                    startDate = "15/01/2019".toLocalDateTime(),
                    leadTime = 0,
                    endDate = "15/01/2019".toLocalDateTime()
                )
            )
    }

}
