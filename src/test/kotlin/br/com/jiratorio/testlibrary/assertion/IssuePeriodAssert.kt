package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import org.assertj.core.api.AbstractAssert
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
