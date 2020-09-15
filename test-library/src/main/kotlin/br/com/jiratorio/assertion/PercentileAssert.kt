package br.com.jiratorio.assertion

import br.com.jiratorio.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.Percentile
import org.assertj.core.api.AbstractAssert

class PercentileAssert(
    actual: Percentile,
) : AbstractAssert<PercentileAssert, Percentile>(
    actual,
    PercentileAssert::class.java
) {

    fun hasAverage(average: Double): PercentileAssert {
        if (actual.average != average) {
            failWithMessage(shouldBeEquals(actual.average, average).create())
        }

        return this
    }

    fun hasMedian(median: Long): PercentileAssert {
        if (actual.median != median) {
            failWithMessage(shouldBeEquals(actual.median, median).create())
        }

        return this
    }

    fun hasPercentile75(percentile75: Long): PercentileAssert {
        if (actual.percentile75 != percentile75) {
            failWithMessage(shouldBeEquals(actual.percentile75, percentile75).create())
        }

        return this
    }

    fun hasPercentile90(percentile90: Long): PercentileAssert {
        if (actual.percentile90 != percentile90) {
            failWithMessage(shouldBeEquals(actual.percentile90, percentile90).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: Percentile): PercentileAssert =
            PercentileAssert(actual)

    }

}
