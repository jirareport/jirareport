package br.com.jiratorio.domain

import java.time.LocalDate

data class LeadTimeComparisonByPeriod(
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val leadTimeName: String,
    val leadTime: Double,
)
