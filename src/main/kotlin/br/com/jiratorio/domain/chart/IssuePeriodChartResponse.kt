package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.entity.embedded.Chart

data class IssuePeriodChartResponse(
    val throughputByEstimate: ThroughputByEstimate,
    val leadTimeCompareChart: LeadTimeCompareChart,
    val leadTime: Chart<String, String>,
    val throughput: Chart<String, Int>
)
