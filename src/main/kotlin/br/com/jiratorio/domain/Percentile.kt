package br.com.jiratorio.domain

data class Percentile(
    val average: Double = 0.0,
    val median: Long = 0,
    val percentile75: Long = 0,
    val percentile90: Long = 0,
)
