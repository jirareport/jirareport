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
                Changelog(to = "COLUMN_ONE", leadTime = 1),
                Changelog(to = "IMP_COLUMN_ONE", leadTime = 2),
                Changelog(to = "COLUMN_TWO", leadTime = 3),
                Changelog(to = "IMP_COLUMN_TWO", leadTime = 4),
                Changelog(to = "COLUMN_THREE", leadTime = 5),
                Changelog(to = "IMP_COLUMN_THREE", leadTime = 6),
                Changelog(to = "COLUMN_FOUR", leadTime = 7)
        )
        val columns = asList("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        val timeInImpediment = calculator.timeInImpediment(changelog, columns)

        assertThat(timeInImpediment).isEqualTo(12)
    }

}
