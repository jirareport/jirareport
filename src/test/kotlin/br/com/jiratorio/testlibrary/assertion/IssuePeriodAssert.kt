package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.chart.DynamicChart
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty
import org.assertj.core.error.ShouldContain.shouldContain
import java.time.LocalDate

class IssuePeriodAssert(
    actual: IssuePeriodEntity,
) : AbstractAssert<IssuePeriodAssert, IssuePeriodEntity>(
    actual,
    IssuePeriodAssert::class.java
) {

    fun hasStartDate(startDate: LocalDate): IssuePeriodAssert {
        if (actual.startDate != startDate) {
            failWithMessage(shouldBeEquals(actual.startDate, startDate).create())
        }

        return this
    }

    fun hasEndDate(endDate: LocalDate): IssuePeriodAssert {
        if (actual.endDate != endDate) {
            failWithMessage(shouldBeEquals(actual.endDate, endDate).create())
        }

        return this
    }

    fun hasLeadTime(leadTime: Double): IssuePeriodAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    val histogram: HistogramAssert
        get() = HistogramAssert(actual.histogram ?: throw NullPointerException())

    fun hasLeadTimeByEstimate(vararg leadTimeByEstimate: Pair<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeByEstimate?.data != mapOf(*leadTimeByEstimate)) {
            failWithMessage(shouldBeEquals(actual.leadTimeByEstimate?.data, leadTimeByEstimate).create())
        }

        return this
    }

    fun hasThroughputByEstimate(vararg throughputByEstimate: Pair<String, Int>): IssuePeriodAssert {
        if (actual.throughputByEstimate?.data != mapOf(*throughputByEstimate)) {
            failWithMessage(shouldBeEquals(actual.throughputByEstimate?.data, throughputByEstimate).create())
        }

        return this
    }

    fun hasLeadTimeBySystem(vararg leadTimeBySystem: Pair<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeBySystem?.data != mapOf(*leadTimeBySystem)) {
            failWithMessage(shouldBeEquals(actual.leadTimeBySystem?.data, leadTimeBySystem).create())
        }

        return this
    }

    fun hasThroughputBySystem(vararg throughputBySystem: Pair<String, Int>): IssuePeriodAssert {
        if (actual.throughputBySystem?.data != mapOf(*throughputBySystem)) {
            failWithMessage(shouldBeEquals(actual.throughputBySystem?.data, throughputBySystem).create())
        }

        return this
    }

    fun hasLeadTimeByType(vararg leadTimeByType: Pair<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeByType?.data != mapOf(*leadTimeByType)) {
            failWithMessage(shouldBeEquals(actual.leadTimeByType?.data, leadTimeByType).create())
        }

        return this
    }

    fun hasThroughputByType(vararg throughputByType: Pair<String, Int>): IssuePeriodAssert {
        if (actual.throughputByType?.data != mapOf(*throughputByType)) {
            failWithMessage(shouldBeEquals(actual.throughputByType?.data, throughputByType).create())
        }

        return this
    }

    fun hasLeadTimeByProject(vararg leadTimeByProject: Pair<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeByProject?.data != mapOf(*leadTimeByProject)) {
            failWithMessage(shouldBeEquals(actual.leadTimeByProject?.data, leadTimeByProject).create())
        }

        return this
    }

    fun hasThroughputByProject(vararg throughputByProject: Pair<String, Int>): IssuePeriodAssert {
        if (actual.throughputByProject?.data != mapOf(*throughputByProject)) {
            failWithMessage(shouldBeEquals(actual.throughputByProject?.data, throughputByProject).create())
        }

        return this
    }

    fun hasLeadTimeByPriority(vararg leadTimeByPriority: Pair<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeByPriority?.data != mapOf(*leadTimeByPriority)) {
            failWithMessage(shouldBeEquals(actual.leadTimeByPriority?.data, leadTimeByPriority).create())
        }

        return this
    }

    fun hasThroughputByPriority(vararg throughputByPriority: Pair<String, Int>): IssuePeriodAssert {
        if (actual.throughputByPriority?.data != mapOf(*throughputByPriority)) {
            failWithMessage(shouldBeEquals(actual.throughputByPriority?.data, throughputByPriority).create())
        }

        return this
    }

    fun hasThroughput(throughput: Int): IssuePeriodAssert {
        if (actual.throughput != throughput) {
            failWithMessage(shouldBeEquals(actual.throughput, throughput).create())
        }

        return this
    }

    fun hasWipAvg(wipAvg: Double): IssuePeriodAssert {
        if (actual.wipAvg != wipAvg) {
            failWithMessage(shouldBeEquals(actual.wipAvg, wipAvg).create())
        }

        return this
    }

    fun hasAvgPctEfficiency(avgPctEfficiency: Double): IssuePeriodAssert {
        if (actual.avgPctEfficiency != avgPctEfficiency) {
            failWithMessage(shouldBeEquals(actual.avgPctEfficiency, avgPctEfficiency).create())
        }

        return this
    }

    fun hasEmptyDynamicCharts(): IssuePeriodAssert {
        if (!actual.dynamicCharts.isNullOrEmpty()) {
            failWithMessage(shouldBeEmpty(actual.dynamicCharts).create())
        }

        return this
    }

    fun hasDynamicCharts(dynamicCharts: List<DynamicChart>? = null): IssuePeriodAssert {
        val notFound = dynamicCharts?.filter { actual.dynamicCharts?.contains(it) != true } ?: emptyList()

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.dynamicCharts, dynamicCharts, notFound).create())
        }

        return this
    }

    fun hasEmptyLeadTimeCompareChart(): IssuePeriodAssert {
        if (!actual.leadTimeCompareChart?.data.isNullOrEmpty()) {
            failWithMessage(shouldBeEmpty(actual.leadTimeCompareChart?.data).create())
        }

        return this
    }

    fun hasLeadTimeCompareChart(leadTimeCompareChart: Map<String, Double>): IssuePeriodAssert {
        if (actual.leadTimeCompareChart?.data != leadTimeCompareChart) {
            failWithMessage(shouldBeEquals(actual.leadTimeCompareChart, leadTimeCompareChart).create())
        }

        return this
    }

    fun containsColumnTimeAvg(vararg columnTimeAverages: ColumnTimeAverageEntity): IssuePeriodAssert {
        val notFound = columnTimeAverages.filter { first ->
            actual.columnTimeAverages.firstOrNull { second ->
                first.columnName == second.columnName && first.averageTime == second.averageTime
            } == null
        }

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.columnTimeAverages, columnTimeAverages, notFound).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssuePeriodEntity): IssuePeriodAssert =
            IssuePeriodAssert(actual)

    }

}
