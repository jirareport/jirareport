package br.com.jiratorio.assertion.response

import br.com.jiratorio.assertion.error.ShouldBeEquals
import br.com.jiratorio.domain.chart.DynamicChart
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldContain.shouldContain

class IssuePeriodDetailResponseAssert private constructor(
    actual: IssuePeriodDetailResponse,
) : AbstractAssert<IssuePeriodDetailResponseAssert, IssuePeriodDetailResponse>(
    actual,
    IssuePeriodDetailResponseAssert::class.java
) {

    fun hasName(name: String): IssuePeriodDetailResponseAssert {
        if (actual.name != name) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasLeadTime(leadTime: Double): IssuePeriodDetailResponseAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    fun hasThroughput(throughput: Int): IssuePeriodDetailResponseAssert {
        if (actual.throughput != throughput) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughput, throughput).create())
        }

        return this
    }

    fun hasLeadTimeByEstimate(leadTimeByEstimate: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeByEstimate != leadTimeByEstimate) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeByEstimate, leadTimeByEstimate).create())
        }

        return this
    }

    fun hasThroughputByEstimate(throughputByEstimate: Chart<String, Int>?): IssuePeriodDetailResponseAssert {
        if (actual.throughputByEstimate != throughputByEstimate) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughputByEstimate, throughputByEstimate).create())
        }

        return this
    }

    fun hasLeadTimeBySystem(leadTimeBySystem: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeBySystem != leadTimeBySystem) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeBySystem, leadTimeBySystem).create())
        }

        return this
    }

    fun hasThroughputBySystem(throughputBySystem: Chart<String, Int>?): IssuePeriodDetailResponseAssert {
        if (actual.throughputBySystem != throughputBySystem) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughputBySystem, throughputBySystem).create())
        }

        return this
    }

    fun hasLeadTimeByType(leadTimeByType: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeByType != leadTimeByType) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeByType, leadTimeByType).create())
        }

        return this
    }

    fun hasThroughputByType(throughputByType: Chart<String, Int>?): IssuePeriodDetailResponseAssert {
        if (actual.throughputByType != throughputByType) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughputByType, throughputByType).create())
        }

        return this
    }

    fun hasLeadTimeByProject(leadTimeByProject: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeByProject != leadTimeByProject) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeByProject, leadTimeByProject).create())
        }

        return this
    }

    fun hasThroughputByProject(throughputByProject: Chart<String, Int>?): IssuePeriodDetailResponseAssert {
        if (actual.throughputByProject != throughputByProject) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughputByProject, throughputByProject).create())
        }

        return this
    }

    fun hasLeadTimeByPriority(leadTimeByPriority: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeByPriority != leadTimeByPriority) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeByPriority, leadTimeByPriority).create())
        }

        return this
    }

    fun hasThroughputByPriority(throughputByPriority: Chart<String, Int>?): IssuePeriodDetailResponseAssert {
        if (actual.throughputByPriority != throughputByPriority) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.throughputByPriority, throughputByPriority).create())
        }

        return this
    }

    fun hasColumnTimeAverages(columnTimeAvg: Collection<ColumnTimeAverageEntity>): IssuePeriodDetailResponseAssert {
        val notFound = columnTimeAvg.filter { columnTimeAverageEntity ->
            actual.columnTimeAverages?.firstOrNull { columnTimeAverageResponse ->
                columnTimeAverageEntity.columnName == columnTimeAverageResponse.columnName && columnTimeAverageEntity.averageTime == columnTimeAverageResponse.averageTime
            } == null
        }

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.columnTimeAverages, columnTimeAvg, notFound).create())
        }

        return this
    }

    fun hasLeadTimeCompareChart(leadTimeCompareChart: Chart<String, Double>?): IssuePeriodDetailResponseAssert {
        if (actual.leadTimeCompareChart != leadTimeCompareChart) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.leadTimeCompareChart, leadTimeCompareChart).create())
        }

        return this
    }

    fun hasDynamicCharts(dynamicCharts: MutableList<DynamicChart>?): IssuePeriodDetailResponseAssert {
        if (actual.dynamicCharts != dynamicCharts) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.dynamicCharts, dynamicCharts).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssuePeriodDetailResponse): IssuePeriodDetailResponseAssert =
            IssuePeriodDetailResponseAssert(actual)

    }

}
