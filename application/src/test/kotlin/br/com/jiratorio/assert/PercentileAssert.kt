package br.com.jiratorio.assert

import br.com.jiratorio.domain.Percentile

class PercentileAssert(
    actual: Percentile
) : BaseAssert<PercentileAssert, Percentile>(
    actual,
    PercentileAssert::class
) {

    fun hasAverage(average: Double) = assertAll {
        objects.assertEqual(field("percentile.average"), actual.average, average)
    }

    fun hasMedian(median: Long) = assertAll {
        objects.assertEqual(field("percentile.median"), actual.median, median)
    }

    fun hasPercentile75(percentile75: Long) = assertAll {
        objects.assertEqual(field("percentile.percentile75"), actual.percentile75, percentile75)
    }

    fun hasPercentile90(percentile90: Long) = assertAll {
        objects.assertEqual(field("percentile.percentile90"), actual.percentile90, percentile90)
    }

}

fun Percentile.assertThat(assertions: PercentileAssert.() -> Unit): PercentileAssert =
    PercentileAssert(this).assertThat(assertions)
