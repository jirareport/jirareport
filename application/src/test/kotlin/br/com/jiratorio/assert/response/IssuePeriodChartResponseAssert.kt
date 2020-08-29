package br.com.jiratorio.assert.response

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.extension.decimal.format

class IssuePeriodChartResponseAssert(
    actual: IssuePeriodChartResponse
) : BaseAssert<IssuePeriodChartResponseAssert, IssuePeriodChartResponse>(
    actual,
    IssuePeriodChartResponseAssert::class
) {

    fun hasThroughputByEstimateLabelsSize(size: Int) = assertAll {
        iterables.assertHasSize(field("throughputByEstimate.labels"), actual.throughputByEstimate.labels, size)
    }

    fun hasThroughputByEstimateSize(estimate: String, size: Int) = assertAll {
        iterables.assertHasSize(
            field("throughputByEstimate.datasources"),
            actual.throughputByEstimate.datasources[estimate],
            size
        )
    }

    fun hasLeadTimeCompareChartLabelsSize(size: Int) = assertAll {
        iterables.assertHasSize(field("leadTimeCompareChart.labels"), actual.leadTimeCompareChart.labels, size)
    }

    fun hasLeadTimeCompareChartSize(key: String, size: Int) {
        iterables.assertHasSize(
            field("leadTimeCompareChart.labels"),
            actual.leadTimeCompareChart.datasources[key],
            size
        )
    }

    fun hasLeadTimeCompareChartData(key: String, leadTime: Double?) = assertAll {
        objects.assertEqual(
            field("leadTimeCompareChart.datasources"),
            actual.leadTimeCompareChart.datasources[key]?.last(),
            leadTime
        )
    }

    fun hasLeadTime(key: String, leadTime: Double) = assertAll {
        objects.assertEqual(field("leadTime"), actual.leadTime.data[key], leadTime.format())
    }

    fun hasThroughput(key: String, throughput: Int) = assertAll {
        objects.assertEqual(field("throughput"), actual.throughput.data[key], throughput)
    }

}

fun IssuePeriodChartResponse.assertThat(assertions: IssuePeriodChartResponseAssert.() -> Unit): IssuePeriodChartResponseAssert =
    IssuePeriodChartResponseAssert(this).assertThat(assertions)
