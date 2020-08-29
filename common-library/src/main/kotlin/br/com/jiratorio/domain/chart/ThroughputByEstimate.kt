package br.com.jiratorio.domain.chart

data class ThroughputByEstimate(

    val labels: Set<String>,

    val datasources: Map<String, List<Int>>

)
