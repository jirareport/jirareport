package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Arrays

internal class ImpedimentCalculatorByFlagTest {

    private var calculator: ImpedimentCalculatorByFlag? = null

    @BeforeEach
    fun setUp() {
        calculator = ImpedimentCalculatorByFlag()
    }

    @Test
    fun testTimeInImpediment() {
        val changelogItems = Arrays.asList(
                JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 1, 12, 0)),
                JiraChangelogItem("customfield_123", null, null, null, null, null, null),
                JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 10, 12, 0)),
                JiraChangelogItem("xablau", null, null, null, null, null, null),
                JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                JiraChangelogItem("bla", null, null, null, null, null, null),
                JiraChangelogItem("other", null, null, null, null, null, null),
                JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 19, 12, 0))
        )

        Assertions.assertThat(calculator!!.timeInImpediment(changelogItems, LocalDateTime.now(), emptyList<LocalDate>(), true))
                .isEqualTo(15)
    }

    @Test
    fun testTimeInImpedimentWithoutTerm() {
        val changelogItems = Arrays.asList(
                JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 5, 12, 0)),
                JiraChangelogItem("customfield_123", null, null, null, null, null, null),
                JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 9, 12, 0)),
                JiraChangelogItem("xablau", null, null, null, null, null, null),
                JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                JiraChangelogItem("bla", null, null, null, null, null, null),
                JiraChangelogItem("other", null, null, null, null, null, null)
        )

        val endDate = LocalDateTime.of(2019, 1, 19, 12, 0)
        Assertions.assertThat(calculator!!.timeInImpediment(changelogItems, endDate, emptyList<LocalDate>(), true))
                .isEqualTo(10)
    }
}
