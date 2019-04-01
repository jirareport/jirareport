package br.com.jiratorio.domain.response.issueperiod

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg

data class IssuePeriodDetailResponse(
    val dates: String,
    val leadTime: Double,
    val issuesCount: Int,

    val leadTimeByEstimate: Chart<String, Double>?,
    val throughputByEstimate: Chart<String, Long>?,

    val leadTimeBySystem: Chart<String, Double>?,
    val throughputBySystem: Chart<String, Long>?,

    val leadTimeByType: Chart<String, Double>?,
    val throughputByType: Chart<String, Long>?,

    val leadTimeByProject: Chart<String, Double>?,
    val throughputByProject: Chart<String, Long>?,

    val leadTimeByPriority: Chart<String, Double>?,
    val throughputByPriority: Chart<String, Long>?,

    val columnTimeAvg: MutableList<ColumnTimeAvg>?,

    val leadTimeCompareChart: Chart<String, Double>?,

    val dynamicCharts: MutableList<DynamicChart>?
)
