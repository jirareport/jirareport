package br.com.jiratorio.extension.time

import br.com.jiratorio.config.junit.testtype.UnitTest
import br.com.jiratorio.extension.toLocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate

@UnitTest
internal class LocalDateExtensionKtTest {

    @ParameterizedTest
    @CsvSource(
        "01/01/2019, true",
        "02/01/2019, false",
        "31/01/2019, false",
        "01/05/2019, true",
        "21/06/2019, false",
        "25/12/2019, true",
        "26/12/2019, false"
    )
    fun `test is holiday`(date: String, expected: Boolean) {
        val commonHolidays = commonHolidays()
        assertThat(date.toLocalDate().isHoliday(commonHolidays))
            .isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2019, false",
        "02/01/2019, true",
        "06/01/2019, false",
        "31/01/2019, true",
        "01/05/2019, false",
        "12/05/2019, false",
        "21/06/2019, true",
        "25/12/2019, false",
        "26/12/2019, true"
    )
    fun `test is work day`(date: String, expected: Boolean) {
        val commonHolidays = commonHolidays()
        assertThat(date.toLocalDate().isWorkday(commonHolidays))
            .isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2019, 01/01/2019, 1",
        "01/01/2019, 02/01/2019, 2",
        "01/01/2019, 31/01/2019, 31",
        "29/04/2019, 03/05/2019, 5"
    )
    fun `test days diff without rest days`(start: String, end: String, expected: Long) {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        val daysDiff = startDate.daysDiff(endDate, emptyList(), true)

        assertThat(daysDiff)
            .isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2019, 01/01/2019, 0",
        "01/01/2019, 02/01/2019, 1",
        "01/01/2019, 31/01/2019, 22",
        "29/04/2019, 03/05/2019, 4",
        "01/03/2019, 04/03/2019, 2"
    )
    fun `test days diff with rest days`(start: String, end: String, expected: Long) {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        val daysDiff = startDate.daysDiff(endDate, commonHolidays(), false)

        assertThat(daysDiff)
            .isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "06/04/2020, 06/04/2020, 07/04/2020",
        "06/04/2020, 05/04/2020, 06/04/2020",
        "06/04/2020, 05/04/2020, 07/04/2020",
        "06/04/2020, 01/04/2020, 30/04/2020"
    )
    fun `should be between`(date: String, start: String, end: String) {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()
        val localDate = date.toLocalDate()

        val isBetween = localDate.isBetween(startDate, endDate)

        assertTrue(isBetween)
    }

    @ParameterizedTest
    @CsvSource(
        "05/04/2020, 06/04/2020, 07/04/2020",
        "07/04/2020, 05/04/2020, 06/04/2020",
        "01/04/2020, 05/04/2020, 07/04/2020",
        "01/03/2020, 01/04/2020, 30/04/2020"
    )
    fun `should not be between`(date: String, start: String, end: String) {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()
        val localDate = date.toLocalDate()

        val isBetween = localDate.isBetween(startDate, endDate)

        assertFalse(isBetween)
    }

    @ParameterizedTest
    @CsvSource(
        "31/01/2020",
        "29/02/2020",
        "31/03/2020",
        "30/04/2020",
        "31/05/2020",
        "30/06/2020",
        "31/07/2020",
        "31/08/2020",
        "30/09/2020",
        "31/10/2020",
        "30/11/2020",
        "31/12/2020"
    )
    fun `should be lastDayOfMonth`(date: String) {
        val lastDayOfMonth = date.toLocalDate().isLastDayOfMonth
        assertTrue(lastDayOfMonth)
    }

    @ParameterizedTest
    @CsvSource(
        "30/01/2020",
        "28/02/2020",
        "15/03/2020",
        "07/04/2020",
        "20/05/2020",
        "01/06/2020",
        "10/07/2020",
        "11/08/2020",
        "29/09/2020",
        "14/10/2020",
        "13/11/2020",
        "09/12/2020"
    )
    fun `shouldn't be lastDayOfMonth`(date: String) {
        val localDate = date.toLocalDate()
        val lastDayOfMonth = localDate.isLastDayOfMonth
        assertFalse(lastDayOfMonth)
    }

    private fun commonHolidays(): List<LocalDate> {
        return listOf(
            "01/01/2019".toLocalDate(),
            "01/05/2019".toLocalDate(),
            "25/12/2019".toLocalDate()
        )
    }

}
