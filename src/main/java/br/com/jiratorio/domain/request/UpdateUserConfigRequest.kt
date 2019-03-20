package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.ChartType

class UpdateUserConfigRequest {

    var state: String? = null

    var city: String? = null

    var holidayToken: String? = null

    var leadTimeChartType: ChartType? = null

    var throughputChartType: ChartType? = null

}
