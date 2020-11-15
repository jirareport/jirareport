package br.com.jiratorio.strategy.impediment

import br.com.jiratorio.domain.changelog.Changelog
import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import br.com.jiratorio.domain.changelog.FieldChangelog
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
class ImpedimentCalculatorByFlagTest {

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

        val (impedimentTime, impedimentHistory) = ImpedimentCalculatorByFlag
            .calcImpediment(null, Changelog(fieldChangelog.toSet()), LocalDateTime.now(), emptyList(), true)

        assertThat(impedimentTime)
            .isEqualTo(15)
        assertThat(impedimentHistory)
            .hasSize(2)
            .containsExactly(
                InternalImpedimentHistory(
                    startDate = "01/01/2019 12:00".toLocalDateTime(),
                    endDate = "10/01/2019 12:00".toLocalDateTime(),
                    leadTime = 10
                ),
                InternalImpedimentHistory(
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

        val (impedimentTime, impedimentHistory) = ImpedimentCalculatorByFlag.calcImpediment(
            null,
            Changelog(fieldChangelog),
            "19/01/2019 12:00".toLocalDateTime(),
            emptyList(),
            true
        )

        assertThat(impedimentTime)
            .isEqualTo(10)

        assertThat(impedimentHistory)
            .hasSize(2)
            .containsExactly(
                InternalImpedimentHistory(
                    startDate = "05/01/2019 12:00".toLocalDateTime(),
                    endDate = "09/01/2019 12:00".toLocalDateTime(),
                    leadTime = 5
                ),
                InternalImpedimentHistory(
                    startDate = "15/01/2019 12:00".toLocalDateTime(),
                    endDate = "19/01/2019 12:00".toLocalDateTime(),
                    leadTime = 5
                )
            )
    }

}
