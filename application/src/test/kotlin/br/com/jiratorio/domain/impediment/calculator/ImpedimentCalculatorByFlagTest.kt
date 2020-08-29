package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.config.junit.testtype.UnitTest
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.parsed.ParsedChangelog
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
internal class ImpedimentCalculatorByFlagTest {

    @Test
    fun `time in impediment`() {
        val fieldChangelog = setOf(
            FieldChangelog(
                field = "flagged",
                to = "impediment",
                created = "01/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "customfield_123",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "flagged",
                created = "10/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "xablau",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "flagged",
                to = "impediment",
                created = "15/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "other",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "flagged",
                created = "19/01/2019 12:00".toLocalDateTime()
            )
        )

        val impedimentCalculatorResult = ImpedimentCalculatorByFlag.calcImpediment(
            null,
            ParsedChangelog(fieldChangelog.toSet()),
            LocalDateTime.now(),
            emptyList(),
            true
        )

        assertThat(impedimentCalculatorResult.timeInImpediment)
            .isEqualTo(15)
        assertThat(impedimentCalculatorResult.impedimentHistory)
            .hasSize(2)
            .containsExactly(
                ImpedimentHistory(
                    startDate = "01/01/2019 12:00".toLocalDateTime(),
                    endDate = "10/01/2019 12:00".toLocalDateTime(),
                    leadTime = 10
                ),
                ImpedimentHistory(
                    startDate = "15/01/2019 12:00".toLocalDateTime(),
                    endDate = "19/01/2019 12:00".toLocalDateTime(),
                    leadTime = 5
                )
            )
    }

    @Test
    fun `time in impediment without term`() {
        val fieldChangelog = setOf(
            FieldChangelog(
                field = "flagged",
                to = "impediment",
                created = "05/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "customfield_123",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "flagged",
                created = "09/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "xablau",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "flagged",
                to = "impediment",
                created = "15/01/2019 12:00".toLocalDateTime()
            ),
            FieldChangelog(
                field = "bla",
                created = LocalDateTime.now()
            ),
            FieldChangelog(
                field = "other",
                created = LocalDateTime.now()
            )
        )

        val impedimentCalculatorResult = ImpedimentCalculatorByFlag.calcImpediment(
            null,
            ParsedChangelog(fieldChangelog),
            "19/01/2019 12:00".toLocalDateTime(),
            emptyList(),
            true
        )

        assertThat(impedimentCalculatorResult.timeInImpediment)
            .isEqualTo(10)
        assertThat(impedimentCalculatorResult.impedimentHistory)
            .hasSize(2)
            .containsExactly(
                ImpedimentHistory(
                    startDate = "05/01/2019 12:00".toLocalDateTime(),
                    endDate = "09/01/2019 12:00".toLocalDateTime(),
                    leadTime = 5
                ),
                ImpedimentHistory(
                    startDate = "15/01/2019 12:00".toLocalDateTime(),
                    endDate = "19/01/2019 12:00".toLocalDateTime(),
                    leadTime = 5
                )
            )
    }

}
