package br.com.jiratorio.domain.chart

data class IssueCountBySize(
    val labels: Set<String>,
    val datasources: Map<String, List<Int>>
)
