package br.com.jiratorio.jira.parser

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.jira.domain.JiraChangelog
import br.com.jiratorio.jira.domain.JiraColumnChangelog
import br.com.jiratorio.junit.testtype.UnitTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
internal class ColumnChangelogParserTest {

    private val columnChangelogParser = ColumnChangelogParser()

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

        val columnChangelog = columnChangelogParser.parse(jiraChangelog, issueCreationDate, emptyList(), true, false)

        Assertions.assertThat(columnChangelog)
            .contains(
                JiraColumnChangelog(
                    from = null,
                    to = "TODO",
                    startDate = "01/01/2019".toLocalDateTime(),
                    leadTime = 5,
                    endDate = "05/01/2019".toLocalDateTime()
                ),
                JiraColumnChangelog(
                    from = "TODO",
                    to = "DEV",
                    startDate = "05/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "10/01/2019".toLocalDateTime()
                ),
                JiraColumnChangelog(
                    from = "DEV",
                    to = "TEST",
                    startDate = "10/01/2019".toLocalDateTime(),
                    leadTime = 6,
                    endDate = "15/01/2019".toLocalDateTime()
                ),
                JiraColumnChangelog(
                    from = "TEST",
                    to = "DONE",
                    startDate = "15/01/2019".toLocalDateTime(),
                    leadTime = 0,
                    endDate = "15/01/2019".toLocalDateTime()
                )
            )
    }

}

