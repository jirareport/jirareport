package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.ChartType

data class UserConfigResponse(

    val username: String,

    val state: String? = null,

    val city: String? = null,

    val holidayToken: String? = null,

    val leadTimeChartType: String = ChartType.BAR.name,

    val throughputChartType: String = ChartType.DOUGHNUT.name

)
