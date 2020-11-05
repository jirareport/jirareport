package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.entity.UserConfigEntity
import org.assertj.core.api.AbstractAssert

class UserConfigAssert(
    actual: UserConfigEntity,
) : AbstractAssert<UserConfigAssert, UserConfigEntity>(
    actual,
    UserConfigAssert::class.java
) {

    fun hasHolidayToken(holidayToken: String?): UserConfigAssert {
        if (actual.holidayToken != holidayToken) {
            failWithMessage(shouldBeEquals(actual.holidayToken, holidayToken).create())
        }

        return this
    }

    fun hasState(state: String?): UserConfigAssert {
        if (actual.state != state) {
            failWithMessage(shouldBeEquals(actual.state, state).create())
        }

        return this
    }

    fun hasCity(city: String): UserConfigAssert {
        if (actual.city != city) {
            failWithMessage(shouldBeEquals(actual.city, city).create())
        }

        return this
    }

    fun hasLeadTimeChartType(leadTimeChartType: ChartType?): UserConfigAssert {
        if (actual.leadTimeChartType != leadTimeChartType) {
            failWithMessage(shouldBeEquals(actual.leadTimeChartType, leadTimeChartType).create())
        }

        return this
    }

    fun hasThroughputChartType(throughputChartType: ChartType?): UserConfigAssert {
        if (actual.throughputChartType != throughputChartType) {
            failWithMessage(shouldBeEquals(actual.throughputChartType, throughputChartType).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: UserConfigEntity): UserConfigAssert =
            UserConfigAssert(actual)

    }

}
