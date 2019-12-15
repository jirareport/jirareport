package br.com.jiratorio.domain.response.issueperiod

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.entity.embedded.Histogram

data class IssuePeriodDetailResponse(

    val dates: String,

    val leadTime: Double,

    val throughput: Int,

    val histogram: Histogram?,

    val leadTimeByEstimate: Chart<String, Double>?,

    val throughputByEstimate: Chart<String, Int>?,

    val leadTimeBySystem: Chart<String, Double>?,

    val throughputBySystem: Chart<String, Int>?,

    val leadTimeByType: Chart<String, Double>?,

    val throughputByType: Chart<String, Int>?,

    val leadTimeByProject: Chart<String, Double>?,

    val throughputByProject: Chart<String, Int>?,

    val leadTimeByPriority: Chart<String, Double>?,

    val throughputByPriority: Chart<String, Int>?,

    val columnTimeAvg: MutableList<ColumnTimeAvg>?,

    val leadTimeCompareChart: Chart<String, Double>?,

    val dynamicCharts: MutableList<DynamicChart>?

)
