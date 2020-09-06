package br.com.jiratorio.domain

import java.time.LocalDate

data class PerformanceComparisonByIssueType(
    val issueType: String?,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val leadTime: Double,
    val throughput: Int,
) 
