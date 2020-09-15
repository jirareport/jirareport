package br.com.jiratorio.assertion

import br.com.jiratorio.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.embedded.Histogram
import org.assertj.core.api.AbstractAssert

class HistogramAssert(
    actual: Histogram?,
) : AbstractAssert<HistogramAssert, Histogram>(
    actual,
    HistogramAssert::class.java
) {

    fun hasMedian(median: Long): HistogramAssert {
        if (actual.median != median) {
            failWithMessage(shouldBeEquals(actual.median, median).create())
        }

        return this
    }

    fun hasPercentile75(percentile75: Long): HistogramAssert {
        if (actual.percentile75 != percentile75) {
            failWithMessage(shouldBeEquals(actual.percentile75, percentile75).create())
        }

        return this
    }

    fun hasPercentile90(percentile90: Long): HistogramAssert {
        if (actual.percentile90 != percentile90) {
            failWithMessage(shouldBeEquals(actual.percentile90, percentile90).create())
        }

        return this
    }

    fun hasChart(vararg chart: Pair<Long, Int>): HistogramAssert {
        if (actual.chart.data != mapOf(*chart)) {
            failWithMessage(shouldBeEquals(actual.chart.data, mapOf(*chart)).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: Histogram?): HistogramAssert =
            HistogramAssert(actual)

    }
}
