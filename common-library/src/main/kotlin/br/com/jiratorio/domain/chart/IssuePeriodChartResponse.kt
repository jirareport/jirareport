package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.entity.embedded.Chart

data class IssuePeriodChartResponse(

    val leadTime: Chart<String, String>,

    val throughput: Chart<String, Int>,

    val throughputByEstimate: ThroughputByEstimate,

    val leadTimeCompareChart: MultiAxisChart<Double>,

    val issueTypePerformanceCompareChart: Map<String, MultiAxisChart<Number>>

)
