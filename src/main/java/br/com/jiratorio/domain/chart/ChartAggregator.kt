package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.entity.embedded.Histogram

data class ChartAggregator(
    var histogram: Histogram? = null,
    var estimated: Chart<String, Long>? = null,
    var leadTimeBySystem: Chart<String, Double>? = null,
    var tasksBySystem: Chart<String, Long>? = null,
    var leadTimeBySize: Chart<String, Double>? = null,
    var columnTimeAvg: MutableList<ColumnTimeAvg>? = null,
    var leadTimeByType: Chart<String, Double>? = null,
    var tasksByType: Chart<String, Long>? = null,
    var leadTimeByProject: Chart<String, Double>? = null,
    var tasksByProject: Chart<String, Long>? = null,
    var leadTimeCompareChart: Chart<String, Double>? = null,
    var leadTimeByPriority: Chart<String, Double>? = null,
    var throughputByPriority: Chart<String, Long>? = null,
    var dynamicCharts: MutableList<DynamicChart>? = null
)
