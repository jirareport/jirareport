package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.Changelog
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.junit.testtype.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
internal class ImpedimentCalculatorByColumnTest {

    @Test
    fun `time in impediment`() {
        val columnChangelog = listOf(
            ColumnChangelogEntity(
                to = "COLUMN_ONE",
                leadTime = 1,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "IMP_COLUMN_ONE",
                leadTime = 2,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "COLUMN_TWO",
                leadTime = 3,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "IMP_COLUMN_TWO",
                leadTime = 4,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "COLUMN_THREE",
                leadTime = 5,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "IMP_COLUMN_THREE",
                leadTime = 6,
                startDate = LocalDateTime.now()
            ),
            ColumnChangelogEntity(
                to = "COLUMN_FOUR",
                leadTime = 7,
                startDate = LocalDateTime.now()
            )
        )
        val columns = listOf("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        val impedimentCalculatorResult = ImpedimentCalculatorByColumn.calcImpediment(
            columns,
            Changelog(columnChangelog = columnChangelog.toSet()),
            LocalDateTime.now(),
            emptyList(),
            true
        )

        assertThat(impedimentCalculatorResult.timeInImpediment)
            .isEqualTo(12)
        assertThat(impedimentCalculatorResult.impedimentHistory)
            .hasSize(3)
            .containsExactly(
                ImpedimentHistoryEntity(
                    startDate = columnChangelog[1].startDate,
                    endDate = columnChangelog[1].endDate,
                    leadTime = columnChangelog[1].leadTime
                ),
                ImpedimentHistoryEntity(
                    startDate = columnChangelog[3].startDate,
                    endDate = columnChangelog[3].endDate,
                    leadTime = columnChangelog[3].leadTime
                ),
                ImpedimentHistoryEntity(
                    startDate = columnChangelog[5].startDate,
                    endDate = columnChangelog[5].endDate,
                    leadTime = columnChangelog[5].leadTime
                )
            )
    }

}