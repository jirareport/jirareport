package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.ChartType

data class UserConfigResponse(
    var state: String? = null,
    var city: String? = null,
    var holidayToken: String? = null,
    var leadTimeChartType: String = ChartType.BAR.name,
    var throughputChartType: String = ChartType.DOUGHNUT.name
)
