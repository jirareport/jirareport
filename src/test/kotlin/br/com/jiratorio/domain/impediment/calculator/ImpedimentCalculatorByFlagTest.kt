package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Arrays.asList

@Tag("unit")
internal class ImpedimentCalculatorByFlagTest {

    @Test
    fun `time in impediment`() {
        val changelogItems = asList(
            JiraChangelogItem(
                field = "flagged",
                toString = "impediment",
                created = "01/01/2019 12:00".toLocalDateTime()
            ),
            JiraChangelogItem(
                field = "customfield_123"
            ),
            JiraChangelogItem(
                field = "flagged", created = "10/01/2019 12:00".toLocalDateTime()
            ),
            JiraChangelogItem(
                field = "xablau"
            ),
            JiraChangelogItem(
                field = "flagged",
                toString = "impediment",
                created = "15/01/2019 12:00".toLocalDateTime()
            ),
            JiraChangelogItem(
                field = "other"
            ),
            JiraChangelogItem(
                field = "flagged", created = "19/01/2019 12:00".toLocalDateTime()
            )
        )

        val timeInImpediment = ImpedimentCalculatorByFlag.timeInImpediment(
            null,
            changelogItems,
            emptyList(),
            LocalDateTime.now(),
            emptyList(),
            true
        )
        assertThat(timeInImpediment).isEqualTo(15)
    }

    @Test
    fun `time in impediment without term`() {
        val changelogItems = asList(
            JiraChangelogItem(
                field = "flagged",
                toString = "impediment",
                created = "05/01/2019 12:00".toLocalDateTime()
            ),
            JiraChangelogItem(field = "customfield_123"),
            JiraChangelogItem(field = "flagged", created = "09/01/2019 12:00".toLocalDateTime()),
            JiraChangelogItem(field = "xablau"),
            JiraChangelogItem(
                field = "flagged",
                toString = "impediment",
                created = "15/01/2019 12:00".toLocalDateTime()
            ),
            JiraChangelogItem(field = "bla"),
            JiraChangelogItem(field = "other")
        )

        val timeInImpediment = ImpedimentCalculatorByFlag.timeInImpediment(
            null,
            changelogItems,
            emptyList(),
            "19/01/2019 12:00".toLocalDateTime(),
            emptyList(),
            true
        )

        assertThat(timeInImpediment).isEqualTo(10)
    }

}
