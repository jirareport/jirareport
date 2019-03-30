package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.embedded.Changelog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Arrays.asList

@Tag("unit")
internal class ImpedimentCalculatorByColumnTest {

    @Test
    fun `time in impediment`() {
        val changelog = asList(
            Changelog(to = "COLUMN_ONE", leadTime = 1, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_ONE", leadTime = 2, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_TWO", leadTime = 3, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_TWO", leadTime = 4, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_THREE", leadTime = 5, created = LocalDateTime.now()),
            Changelog(to = "IMP_COLUMN_THREE", leadTime = 6, created = LocalDateTime.now()),
            Changelog(to = "COLUMN_FOUR", leadTime = 7, created = LocalDateTime.now())
        )
        val columns = asList("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE")

        val timeInImpediment = ImpedimentCalculatorByColumn.timeInImpediment(
            columns,
            emptyList(),
            changelog,
            LocalDateTime.now(),
            null,
            true
        )

        assertThat(timeInImpediment).isEqualTo(12)
    }

}
