package br.com.jiratorio.service.impl

import br.com.jiratorio.assert.PercentileAssert
import br.com.jiratorio.domain.Percentile
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class PercentileServiceImplTest {

    private val percentileServiceImpl = PercentileServiceImpl()

    @ParameterizedTest
    @MethodSource("calculatePercentileProvider")
    fun `test calculate percentile`(pair: Pair<List<Long>, Percentile>) {
        val (leadTimes, expected) = pair

        val result = percentileServiceImpl.calculatePercentile(leadTimes)

        PercentileAssert(result).assertThat {
            hasAverage(expected.average)
            hasMedian(expected.median)
            hasPercentile75(expected.percentile75)
            hasPercentile90(expected.percentile90)
        }
    }

    fun calculatePercentileProvider(): List<Pair<List<Long>, Percentile>> {
        return listOf(
            listOf(1L, 3L, 4L, 2L, 1L, 5L, 3L, 2L) to Percentile(
                2.625,
                2,
                3,
                5
            ),
            listOf(3L, 5L, 5L, 7L, 3L, 3L, 2L, 7L) to Percentile(
                4.375,
                3,
                5,
                7
            ),
            listOf(6L, 16L, 3L, 19L, 4L, 12L, 5L, 1L, 8L, 17L, 18L, 16L, 19L, 6L, 2L, 17L, 6L, 5L, 3L) to Percentile(
                9.631578947368421,
                6,
                17,
                19
            ),
            listOf(7L, 6L, 1L, 18L, 14L, 8L, 11L, 6L, 3L, 8L, 9L, 19L, 13L) to Percentile(
                9.461538461538462,
                8,
                13,
                18
            ),
            listOf(18L, 18L, 7L, 2L, 7L, 5L, 18L, 1L, 6L, 12L, 6L, 17L, 7L, 7L, 14L, 1L, 13L) to Percentile(
                9.352941176470589,
                7,
                14,
                18
            ),
            emptyList<Long>() to Percentile(
                0.0,
                0,
                0,
                0
            )
        )
    }

}
