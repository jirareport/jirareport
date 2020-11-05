package br.com.jiratorio.domain

import java.time.LocalDateTime

interface ImpedimentHistory {

    val startDate: LocalDateTime

    val endDate: LocalDateTime

    val leadTime: Long

}
