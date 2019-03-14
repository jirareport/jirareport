package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.embedded.Changelog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Arrays.asList

internal class ImpedimentCalculatorByColumnTest {

    private val calculator: ImpedimentCalculatorByColumn = ImpedimentCalculatorByColumn()

    @Test
    fun `time in impediment`() {
        val changelog = asList(
                createChangelog("COLUMN_ONE", 1),
                createChangelog("IMP_COLUMN_ONE", 2),
                createChangelog("COLUMN_TWO", 3),
                createChangelog("IMP_COLUMN_TWO", 4),
                createChangelog("COLUMN_THREE", 5),
                createChangelog("IMP_COLUMN_THREE", 6),
                createChangelog("COLUMN_FOUR", 7)
        )
        val columns = asList("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        val timeInImpediment = calculator.timeInImpediment(changelog, columns)

        assertThat(timeInImpediment).isEqualTo(12)
    }

    private fun createChangelog(column: String, leadTime: Long) =
            Changelog(null, null, column, leadTime, null)

}
