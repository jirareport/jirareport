package br.com.jiratorio.domain

data class Percentile(
    var average: Double = 0.0,
    var median: Long = 0,
    var percentile75: Long = 0,
    var percentile90: Long = 0
)
