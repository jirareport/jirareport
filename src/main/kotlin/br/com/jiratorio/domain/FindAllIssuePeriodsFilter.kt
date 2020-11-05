package br.com.jiratorio.domain

import java.time.LocalDate

data class FindAllIssuePeriodsFilter(
    val boardId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
