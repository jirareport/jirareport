package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.response.issue.IssueResponse

data class IssueListResponse(

    val leadTime: Double,

    val throughput: Int,

    val issues: List<IssueResponse>,

    val charts: ChartAggregator,

    val weeklyThroughput: Chart<String, Int>

)
