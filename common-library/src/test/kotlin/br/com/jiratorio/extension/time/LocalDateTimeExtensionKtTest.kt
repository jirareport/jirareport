package br.com.jiratorio.extension.time

import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.junit.testtype.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.LocalDateTime

@UnitTest
internal class LocalDateTimeExtensionKtTest {

    @Nested
    inner class MinutesDiffWithoutIgnoreWeekend {

        @ParameterizedTest
        @MethodSource("testMinutesDiffProvider")
        fun `test minutes diff`(triple: Triple<LocalDateTime, LocalDateTime, Long>) {
            val (startDate, endDate, expectedResult) = triple

            val minutesDiff = startDate.minutesDiff(endDate, commonHolidays(), false)

            assertThat(minutesDiff)
                .isEqualTo(expectedResult)
        }

        fun testMinutesDiffProvider(): List<Triple<LocalDateTime, LocalDateTime, Long>> {
            return listOf(
                Triple(
                    "31/12/2018 12:01".toLocalDateTime(),
                    "09/01/2019 11:08".toLocalDateTime(),
                    8588L
                ),
                Triple(
                    "10/01/2019 00:00:00".toLocalDateTime(),
                    "10/01/2019 23:59:59".toLocalDateTime(),
                    1440L
                ),
                Triple(
                    "25/03/2019 00:00:00".toLocalDateTime(),
                    "29/03/2019 23:59:59".toLocalDateTime(),
                    7200L
                ),
                Triple(
                    "29/04/2018 00:00:00".toLocalDateTime(),
                    "03/05/2018 23:59:59".toLocalDateTime(),
                    5760L
                ),
                Triple(
                    "31/12/2017 23:59:59".toLocalDateTime(),
                    "31/12/2018 23:59:59".toLocalDateTime(),
                    375840L
                )
            )
        }

    }

    @Nested
    inner class MinutesDiffWithIgnoreWeekend {

        @ParameterizedTest
        @MethodSource("testMinutesDiffProvider")
        fun `test minutes diff`(triple: Triple<LocalDateTime, LocalDateTime, Long>) {
            val (startDate, endDate, expectedResult) = triple

            val minutesDiff = startDate.minutesDiff(endDate, emptyList(), true)

            assertThat(minutesDiff)
                .isEqualTo(expectedResult)
        }

        fun testMinutesDiffProvider(): List<Triple<LocalDateTime, LocalDateTime, Long>> {
            return listOf(
                Triple(
                    "31/12/2018 12:01".toLocalDateTime(),
                    "09/01/2019 11:08".toLocalDateTime(),
                    12908L
                ),
                Triple(
                    "10/01/2019 00:00:00".toLocalDateTime(),
                    "10/01/2019 23:59:59".toLocalDateTime(),
                    1440L
                ),
                Triple(
                    "25/03/2019 00:00:00".toLocalDateTime(),
                    "29/03/2019 23:59:59".toLocalDateTime(),
                    7200L
                ),
                Triple(
                    "29/04/2018 00:00:00".toLocalDateTime(),
                    "03/05/2018 23:59:59".toLocalDateTime(),
                    7200L
                ),
                Triple(
                    "01/01/2018 00:00:00".toLocalDateTime(),
                    "31/12/2018 23:59:59".toLocalDateTime(),
                    525600L
                )
            )
        }
    }

    @Nested
    inner class PlusDaysTest {

        @ParameterizedTest
        @CsvSource(
            "01/01/2019, 1, 01/01/2019",
            "01/01/2019, 5, 05/01/2019",
            "01/01/2019, 31, 31/01/2019",
            "29/04/2019, 5, 03/05/2019"
        )
        fun `test plus days without rest days`(start: String, days: Long, expected: String) {
            val result = start.toLocalDateTime().plusDays(days, emptyList(), true)

            assertThat(result)
                .isEqualTo(expected.toLocalDate())
        }

        @ParameterizedTest
        @CsvSource(
            "01/01/2019, 1, 01/01/2019",
            "01/01/2019, 5, 07/01/2019",
            "01/01/2019, 31, 12/02/2019",
            "29/04/2019, 5, 06/05/2019"
        )
        fun `test plus days with rest days`(start: String, days: Long, expected: String) {
            val result = start.toLocalDateTime().plusDays(days, commonHolidays(), false)

            assertThat(result)
                .isEqualTo(expected.toLocalDate())
        }

    }

    private fun commonHolidays(): List<LocalDate> {
        return listOf(
            "01/01/2019".toLocalDate(),
            "01/05/2019".toLocalDate(),
            "25/12/2019".toLocalDate()
        )
    }

}
