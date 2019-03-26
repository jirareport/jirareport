package br.com.jiratorio.assert

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.entity.UserConfig

class UserConfigAssert(actual: UserConfig) :
    BaseAssert<UserConfigAssert, UserConfig>(actual, UserConfigAssert::class) {

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
