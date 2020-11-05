package br.com.jiratorio.testlibrary.assertion.response

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodResponse
import org.assertj.core.api.AbstractAssert

class IssuePeriodResponseAssert private constructor(
    actual: IssuePeriodResponse,
) : AbstractAssert<IssuePeriodResponseAssert, IssuePeriodResponse>(
    actual,
    IssuePeriodResponseAssert::class.java
) {

    fun hasId(id: Long): IssuePeriodResponseAssert {
        if (actual.id != id) {
            failWithMessage(shouldBeEquals(actual.id, id).create())
        }

        return this
    }

    fun hasName(name: String): IssuePeriodResponseAssert {
        if (actual.name != name) {
            failWithMessage(shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasWipAvg(wipAvg: Double): IssuePeriodResponseAssert {
        if (actual.wipAvg != wipAvg) {
            failWithMessage(shouldBeEquals(actual.wipAvg, wipAvg).create())
        }

        return this
    }

    fun hasLeadTime(leadTime: Double): IssuePeriodResponseAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    fun hasAvgPctEfficiency(avgPctEfficiency: Double): IssuePeriodResponseAssert {
        if (actual.avgPctEfficiency != avgPctEfficiency) {
            failWithMessage(shouldBeEquals(actual.avgPctEfficiency, avgPctEfficiency).create())
        }

        return this
    }

    fun hasJql(jql: String): IssuePeriodResponseAssert {
        if (actual.jql != jql) {
            failWithMessage(shouldBeEquals(actual.jql, jql).create())
        }

        return this
    }

    fun hasThroughput(throughput: Int): IssuePeriodResponseAssert {
        if (actual.throughput != throughput) {
            failWithMessage(shouldBeEquals(actual.throughput, throughput).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssuePeriodResponse): IssuePeriodResponseAssert =
            IssuePeriodResponseAssert(actual)

    }

}
