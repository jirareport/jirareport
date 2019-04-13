package br.com.jiratorio.assert

import br.com.jiratorio.domain.entity.embedded.Histogram

class HistogramAssert(actual: Histogram) :
    BaseAssert<HistogramAssert, Histogram>(actual, HistogramAssert::class) {

    fun hasMedian(median: Long) = assertAll {
        objects.assertEqual(field("histogram.median"), actual.median, median)
    }

    fun hasPercentile75(percentile75: Long) = assertAll {
        objects.assertEqual(field("histogram.percentile75"), actual.percentile75, percentile75)
    }

    fun hasPercentile90(percentile90: Long) = assertAll {
        objects.assertEqual(field("histogram.percentile90"), actual.percentile90, percentile90)
    }

    fun hasChart(vararg chart: Pair<Long, Int>) = assertAll {
        objects.assertEqual(field("histogram.chart"), actual.chart.data, mapOf(*chart))
    }

}
