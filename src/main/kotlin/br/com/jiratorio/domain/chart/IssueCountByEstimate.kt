package br.com.jiratorio.domain.chart

data class IssueCountByEstimate(
    val labels: Set<String>,
    val datasources: Map<String, List<Int>>
)
