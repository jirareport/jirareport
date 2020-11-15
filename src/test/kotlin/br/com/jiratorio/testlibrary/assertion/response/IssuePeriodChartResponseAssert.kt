package br.com.jiratorio.testlibrary.assertion.response

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.extension.decimal.format
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldHaveSize.shouldHaveSize

class IssuePeriodChartResponseAssert private constructor(
    actual: IssuePeriodChartResponse,
) : AbstractAssert<IssuePeriodChartResponseAssert, IssuePeriodChartResponse>(
    actual,
    IssuePeriodChartResponseAssert::class.java
) {

    fun hasThroughputByEstimateLabelsSize(size: Int): IssuePeriodChartResponseAssert {
        if (actual.throughputByEstimate.labels.size != size) {
            failWithMessage(shouldHaveSize(actual.throughputByEstimate.labels, actual.throughputByEstimate.labels.size, size).create())
        }

        return this
    }

    fun hasThroughputByEstimateSize(estimate: String, size: Int): IssuePeriodChartResponseAssert {
        if (actual.throughputByEstimate.datasources[estimate]?.size != size) {
            failWithMessage(shouldHaveSize(actual.throughputByEstimate.datasources[estimate], actual.throughputByEstimate.datasources[estimate]?.size ?: 0, size).create())
        }

        return this
    }

    fun hasLeadTimeCompareChartLabelsSize(size: Int): IssuePeriodChartResponseAssert {
        if (actual.leadTimeCompareChart.labels.size != size) {
            failWithMessage(shouldHaveSize(actual.leadTimeCompareChart.labels, actual.leadTimeCompareChart.labels.size, size).create())
        }

        return this
    }

    fun hasLeadTimeCompareChartSize(key: String, size: Int): IssuePeriodChartResponseAssert {
        if (actual.leadTimeCompareChart.datasources[key]?.size != size) {
            failWithMessage(shouldHaveSize(actual.leadTimeCompareChart.datasources[key], actual.leadTimeCompareChart.datasources[key]?.size ?: 0, size).create())
        }

        return this
    }

    fun hasLeadTimeCompareChartData(key: String, leadTime: Double?): IssuePeriodChartResponseAssert {
        if (actual.leadTimeCompareChart.datasources[key]?.last() != leadTime) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeCompareChart.datasources[key]?.last(), leadTime).create())
        }

        return this
    }

    fun hasLeadTime(key: String, leadTime: Double): IssuePeriodChartResponseAssert {
        if (actual.leadTime.data[key] != leadTime.format()) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTime.data[key], leadTime.format()).create())
        }

        return this
    }

    fun hasThroughput(key: String, throughput: Int): IssuePeriodChartResponseAssert {
        if (actual.throughput.data[key] != throughput) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughput.data[key], throughput).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssuePeriodChartResponse): IssuePeriodChartResponseAssert =
            IssuePeriodChartResponseAssert(actual)

    }

}
