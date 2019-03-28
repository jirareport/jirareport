package br.com.jiratorio.extension

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate

@Tag("unit")
internal class LocalDateExtensionKtTest {

    @ParameterizedTest
    @CsvSource(
        "01/01/2018, true",
        "02/01/2018, false",
        "31/01/2018, false",
        "01/05/2018, true",
        "21/06/2018, false",
        "25/12/2018, true",
        "26/12/2018, false"
    )
    fun `test is holiday`(date: String, expected: Boolean) {
        val commonHolidays = commonHolidays()
        assertThat(date.toLocalDate().isHoliday(commonHolidays))
            .isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2018, false",
        "02/01/2018, true",
        "06/01/2018, false",
        "31/01/2018, true",
        "01/05/2018, false",
        "12/05/2018, false",
        "21/06/2018, true",
        "25/12/2018, false",
        "26/12/2018, true"
    )
    fun `test is work day`(date: String, expected: Boolean) {
        val commonHolidays = commonHolidays()
        Assertions.assertThat(date.toLocalDate().isWorkDay(commonHolidays))
            .isEqualTo(expected)
    }

    private fun commonHolidays(): List<LocalDate> {
        return listOf(
            "01/01/2018".toLocalDate(),
            "01/05/2018".toLocalDate(),
            "25/12/2018".toLocalDate()
        )
    }

}
