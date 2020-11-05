package br.com.jiratorio.domain.issue

import java.time.LocalDate

data class MinimalIssuePeriod(
    val id: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jql: String,
    val leadTime: Double,
    val throughput: Int,
    val wipAvg: Double,
    val avgPctEfficiency: Double,
) {
    lateinit var name: String
}
