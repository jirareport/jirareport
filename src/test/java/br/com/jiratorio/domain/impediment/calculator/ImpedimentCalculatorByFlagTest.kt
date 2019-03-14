package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Arrays.asList

internal class ImpedimentCalculatorByFlagTest {

    private val calculator: ImpedimentCalculatorByFlag = ImpedimentCalculatorByFlag()

    @Test
    fun `time in impediment`() {
        val changelogItems = asList(
                createJiraChangelogItem("flagged", "impediment", LocalDateTime.of(2019, 1, 1, 12, 0)),
                createJiraChangelogItem("customfield_123"),
                createJiraChangelogItem("flagged", created = LocalDateTime.of(2019, 1, 10, 12, 0)),
                createJiraChangelogItem("xablau"),
                createJiraChangelogItem("flagged", "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                createJiraChangelogItem("bla"),
                createJiraChangelogItem("other"),
                createJiraChangelogItem("flagged", created = LocalDateTime.of(2019, 1, 19, 12, 0))
        )

        val timeInImpediment = calculator.timeInImpediment(changelogItems, LocalDateTime.now(), emptyList<LocalDate>(), true)
        assertThat(timeInImpediment).isEqualTo(15)
    }

    @Test
    fun `time in impediment without term`() {
        val changelogItems = asList(
                createJiraChangelogItem("flagged", "impediment", LocalDateTime.of(2019, 1, 5, 12, 0)),
                createJiraChangelogItem("customfield_123"),
                createJiraChangelogItem("flagged", created = LocalDateTime.of(2019, 1, 9, 12, 0)),
                createJiraChangelogItem("xablau"),
                createJiraChangelogItem("flagged", "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                createJiraChangelogItem("bla"),
                createJiraChangelogItem("other")
        )

        val endDate = LocalDateTime.of(2019, 1, 19, 12, 0)
        val timeInImpediment = calculator.timeInImpediment(changelogItems, endDate, emptyList<LocalDate>(), true)
        assertThat(timeInImpediment).isEqualTo(10)
    }

    private fun createJiraChangelogItem(field: String? = null, toString: String? = null, created: LocalDateTime? = null) =
            JiraChangelogItem(field, null, null, null, null, toString, created)
}
