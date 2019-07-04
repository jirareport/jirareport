package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.entity.embedded.Histogram

data class ChartAggregator(
    var histogram: Histogram,
    var leadTimeByEstimate: Chart<String, Double>,
    var throughputByEstimate: Chart<String, Int>,
    var leadTimeBySystem: Chart<String, Double>,
    var throughputBySystem: Chart<String, Int>,
    var leadTimeByType: Chart<String, Double>,
    var throughputByType: Chart<String, Int>,
    var leadTimeByProject: Chart<String, Double>,
    var throughputByProject: Chart<String, Int>,
    var leadTimeByPriority: Chart<String, Double>,
    var throughputByPriority: Chart<String, Int>,
    var columnTimeAvg: List<ColumnTimeAvg>,
    var leadTimeCompareChart: Chart<String, Double>,
    var dynamicCharts: List<DynamicChart>
)
