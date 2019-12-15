package br.com.jiratorio.domain.entity.embedded

data class Histogram(

    val chart: Chart<Long, Int>,

    val median: Long,

    val percentile75: Long,

    val percentile90: Long

)
