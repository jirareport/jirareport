package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.embedded.Changelog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@Tag("unit")
internal class ImpedimentCalculatorByColumnTest {

    @Test
    fun `time in impediment`() {
        val changelog = listOf(
            Changelog(to = "COLUMN_ONE", leadTime = 1, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_ONE", leadTime = 2, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_TWO", leadTime = 3, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_TWO", leadTime = 4, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_THREE", leadTime = 5, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_THREE", leadTime = 6, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_FOUR", leadTime = 7, created = LocalDateTime.now())
        )
        val columns = listOf("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        val impedimentCalculatorResult = ImpedimentCalculatorByColumn.calcImpediment(
            columns,
            emptyList(),
            changelog,
            LocalDateTime.now(),
            emptyList(),
            true
        )

        assertThat(impedimentCalculatorResult.timeInImpediment)
            .isEqualTo(12)
        assertThat(impedimentCalculatorResult.impedimentHistory)
            .hasSize(3)
            .containsExactly(
                ImpedimentHistory(
                    startDate = changelog[1].created,
                    endDate = changelog[1].endDate,
                    leadTime = changelog[1].leadTime
                ),
                ImpedimentHistory(
                    startDate = changelog[3].created,
                    endDate = changelog[3].endDate,
                    leadTime = changelog[3].leadTime
                ),
                ImpedimentHistory(
                    startDate = changelog[5].created,
                    endDate = changelog[5].endDate,
                    leadTime = changelog[5].leadTime
                )
            )
    }

}
