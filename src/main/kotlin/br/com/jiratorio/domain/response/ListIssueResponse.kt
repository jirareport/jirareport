package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.embedded.Chart

data class ListIssueResponse(
    val issues: List<IssueResponse>,
    val charts: ChartAggregator,
    val leadTime: Double,
    val weeklyThroughput: Chart<String, Int>
)
