package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
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
        val jiraChangelog = asList(
                JiraChangelogItem(field = "issueType"),
                JiraChangelogItem(field = "status", toString = "TODO", created = "01/01/2019".toLocalDateTime()),
                JiraChangelogItem(field = "other"),
                JiraChangelogItem(field = "status", fromString = "TODO", toString = "DEV", created = "05/01/2019".toLocalDateTime()),
                JiraChangelogItem(field = "flag"),
                JiraChangelogItem(field = "status", fromString = "DEV", toString = "TEST", created = "10/01/2019".toLocalDateTime()),
                JiraChangelogItem(field = "priority"),
                JiraChangelogItem(field = "status", fromString = "TEST", toString = "DONE", created = "15/01/2019".toLocalDateTime()),
                JiraChangelogItem(field = "dueDate")
        )

        assertThat(changelogServiceImpl.parseChangelog(jiraChangelog, emptyList(), true))
                .contains(
                        Changelog(from = null, to = "TODO", created = "01/01/2019".toLocalDateTime(), leadTime = 5, endDate = "05/01/2019".toLocalDateTime()),
                        Changelog(from = "TODO", to = "DEV", created = "05/01/2019".toLocalDateTime(), leadTime = 6, endDate = "10/01/2019".toLocalDateTime()),
                        Changelog(from = "DEV", to = "TEST", created = "10/01/2019".toLocalDateTime(), leadTime = 6, endDate = "15/01/2019".toLocalDateTime()),
                        Changelog(from = "TEST", to = "DONE", created = "15/01/2019".toLocalDateTime(), leadTime = 0, endDate = "15/01/2019".toLocalDateTime())
                )
    }

}
