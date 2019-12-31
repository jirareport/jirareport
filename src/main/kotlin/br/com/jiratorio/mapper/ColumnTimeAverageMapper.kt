package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ColumnTimeAverage
import br.com.jiratorio.domain.response.ColumnTimeAverageResponse

fun List<ColumnTimeAverage>.toResponse(): List<ColumnTimeAverageResponse> =
    map { it.toResponse() }

fun ColumnTimeAverage.toResponse(): ColumnTimeAverageResponse =
    ColumnTimeAverageResponse(
        columnName = columnName,
        averageTime = averageTime
    )
