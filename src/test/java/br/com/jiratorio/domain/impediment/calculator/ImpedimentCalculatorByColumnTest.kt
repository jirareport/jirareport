package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.entity.embedded.Changelog
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Arrays

internal class ImpedimentCalculatorByColumnTest {

    private var calculator: ImpedimentCalculatorByColumn? = null

    @BeforeEach
    fun setUp() {
        calculator = ImpedimentCalculatorByColumn()
    }

    @Test
    fun testTimeInImpediment() {
        val changelogs = Arrays.asList(
                Changelog(null, null, "COLUMN_ONE", 1L, null),
                Changelog(null, null, "IMP_COLUMN_ONE", 2L, null),
                Changelog(null, null, "COLUMN_TWO", 3L, null),
                Changelog(null, null, "IMP_COLUMN_TWO", 4L, null),
                Changelog(null, null, "COLUMN_THREE", 5L, null),
                Changelog(null, null, "IMP_COLUMN_THREE", 6L, null),
                Changelog(null, null, "COLUMN_FOUR", 7L, null)
        )
        val columns = Arrays.asList("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        Assertions.assertThat(calculator!!.timeInImpediment(changelogs, columns))
                .isEqualTo(12);
    }

}
