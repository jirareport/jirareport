package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays.asList

internal class ImpedimentCalculatorByFlagTest {

    @Test
    fun `time in impediment`() {
        val changelogItems = asList(
                JiraChangelogItem(field = "flagged", toString = "impediment", created = at("01/01/2019 12:00")),
                JiraChangelogItem(field = "customfield_123"),
                JiraChangelogItem(field = "flagged", created = at("10/01/2019 12:00")),
                JiraChangelogItem(field = "xablau"),
                JiraChangelogItem(field = "flagged", toString = "impediment", created = at("15/01/2019 12:00")),
                JiraChangelogItem(field = "other"),
                JiraChangelogItem(field = "flagged", created = at("19/01/2019 12:00"))
        )

        val timeInImpediment = ImpedimentCalculatorByFlag.timeInImpediment(changelogItems, LocalDateTime.now(), emptyList(), true)
        assertThat(timeInImpediment).isEqualTo(15)
    }

    @Test
    fun `time in impediment without term`() {
        val changelogItems = asList(
                JiraChangelogItem(field = "flagged", toString = "impediment", created = at("05/01/2019 12:00")),
                JiraChangelogItem(field = "customfield_123"),
                JiraChangelogItem(field = "flagged", created = at("09/01/2019 12:00")),
                JiraChangelogItem(field = "xablau"),
                JiraChangelogItem(field = "flagged", toString = "impediment", created = at("15/01/2019 12:00")),
                JiraChangelogItem(field = "bla"),
                JiraChangelogItem(field = "other")
        )

        val timeInImpediment = ImpedimentCalculatorByFlag.timeInImpediment(changelogItems, at("19/01/2019 12:00"), emptyList(), true)
        assertThat(timeInImpediment).isEqualTo(10)
    }

    private fun at(date: String) = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

}
