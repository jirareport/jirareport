package br.com.jiratorio.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.LocalDateTime

@Tag("unit")
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

        private fun commonHolidays(): List<LocalDate> {
            return listOf(
                "01/01/2019".toLocalDate(),
                "01/05/2019".toLocalDate()
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

}
