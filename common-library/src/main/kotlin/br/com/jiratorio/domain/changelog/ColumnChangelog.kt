package br.com.jiratorio.domain.changelog

import java.time.LocalDateTime

interface ColumnChangelog {

    val from: String?

    val to: String

    val startDate: LocalDateTime

    val endDate: LocalDateTime

    val leadTime: Long

}
