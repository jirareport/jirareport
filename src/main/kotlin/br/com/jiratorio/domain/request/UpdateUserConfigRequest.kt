package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.chart.ChartType

data class UpdateUserConfigRequest(

    val state: String? = null,

    val city: String? = null,

    val holidayToken: String? = null,

    val leadTimeChartType: ChartType? = null,

    val throughputChartType: ChartType? = null

)
