package br.com.jiratorio.domain.entity.embedded

data class Histogram(
    var chart: Chart<Long, Long>? = null,
    var median: Long? = null,
    var percentile75: Long? = null,
    var percentile90: Long? = null
)
