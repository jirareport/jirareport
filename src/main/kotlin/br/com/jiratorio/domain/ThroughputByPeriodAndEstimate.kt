package br.com.jiratorio.domain

import java.time.LocalDate

data class ThroughputByPeriodAndEstimate(
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val estimate: String?,
    val throughput: Int,
)
