package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.entity.embedded.Chart

data class IssuePeriodChartResponse(
    val issueCountBySize: IssueCountBySize,
    val leadTimeCompareChart: LeadTimeCompareChart,
    val leadTime: Chart<String, String>,
    val issuesCount: Chart<String, Int>
)
