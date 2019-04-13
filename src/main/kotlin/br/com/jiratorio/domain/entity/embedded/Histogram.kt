package br.com.jiratorio.domain.entity.embedded

data class Histogram(
    var chart: Chart<Long, Int>,
    var median: Long,
    var percentile75: Long,
    var percentile90: Long
)
