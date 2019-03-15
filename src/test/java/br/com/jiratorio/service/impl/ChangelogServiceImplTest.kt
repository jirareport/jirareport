package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays.asList

@Tag("unit")
internal class ChangelogServiceImplTest {

    private val changelogServiceImpl = ChangelogServiceImpl()

    @Test
    fun `parse changelog`() {
        val atDay: (day: String) -> LocalDateTime = { LocalDate.parse(it, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay() }

        val jiraChangelog = asList(
                JiraChangelogItem(field = "issueType"),
                JiraChangelogItem(field = "status", toString = "TODO", created = atDay("01/01/2019")),
                JiraChangelogItem(field = "other"),
                JiraChangelogItem(field = "status", fromString = "TODO", toString = "DEV", created = atDay("05/01/2019")),
                JiraChangelogItem(field = "flag"),
                JiraChangelogItem(field = "status", fromString = "DEV", toString = "TEST", created = atDay("10/01/2019")),
                JiraChangelogItem(field = "priority"),
                JiraChangelogItem(field = "status", fromString = "TEST", toString = "DONE", created = atDay("15/01/2019")),
                JiraChangelogItem(field = "dueDate")
        )

        assertThat(changelogServiceImpl.parseChangelog(jiraChangelog, emptyList(), true))
                .contains(
                        Changelog(from = null, to = "TODO", created = atDay("01/01/2019"), leadTime = 5, endDate = atDay("05/01/2019")),
                        Changelog(from = "TODO", to = "DEV", created = atDay("05/01/2019"), leadTime = 6, endDate = atDay("10/01/2019")),
                        Changelog(from = "DEV", to = "TEST", created = atDay("10/01/2019"), leadTime = 6, endDate = atDay("15/01/2019")),
                        Changelog(from = "TEST", to = "DONE", created = atDay("15/01/2019"), leadTime = 0, endDate = atDay("15/01/2019"))
                )
    }

}
