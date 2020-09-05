package br.com.jiratorio.assertion

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.entity.UserConfigEntity

class UserConfigAssert(
    actual: UserConfigEntity
) : BaseAssert<UserConfigAssert, UserConfigEntity>(
    actual,
    UserConfigAssert::class
) {

    fun hasHolidayToken(holidayToken: String) = assertAll {
        objects.assertEqual(field("userConfig.holidayToken"), actual.holidayToken, holidayToken)
    }

    fun hasState(state: String) = assertAll {
        objects.assertEqual(field("userConfig.state"), actual.state, state)
    }

    fun hasCity(city: String) = assertAll {
        objects.assertEqual(field("userConfig.city"), actual.city, city)
    }

    fun hasLeadTimeChartType(leadTimeChartType: ChartType) = assertAll {
        objects.assertEqual(field("userConfig.leadTimeChartType"), actual.leadTimeChartType, leadTimeChartType)
    }

    fun hasThroughputChartType(throughputChartType: ChartType) = assertAll {
        objects.assertEqual(field("userConfig.throughputChartType"), actual.throughputChartType, throughputChartType)
    }

}

fun UserConfigEntity.assertThat(assertions: UserConfigAssert.() -> Unit): UserConfigAssert =
    UserConfigAssert(this).assertThat(assertions)
