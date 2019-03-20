package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.ChartType

class UserConfigResponse {

    var state: String? = null

    var city: String? = null

    var holidayToken: String? = null

    var leadTimeChartType = ChartType.BAR.name

    var throughputChartType = ChartType.DOUGHNUT.name

}
