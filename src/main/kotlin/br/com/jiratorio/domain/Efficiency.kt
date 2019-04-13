package br.com.jiratorio.domain

data class Efficiency(
    var waitTime: Long = 0L,
    var touchTime: Long = 0L,
    var pctEfficiency: Double = 0.0
)
