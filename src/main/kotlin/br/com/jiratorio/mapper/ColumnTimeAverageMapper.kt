package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.response.ColumnTimeAverageResponse

fun Collection<ColumnTimeAverageEntity>.toResponse(): List<ColumnTimeAverageResponse> =
    map { it.toResponse() }

fun ColumnTimeAverageEntity.toResponse(): ColumnTimeAverageResponse =
    ColumnTimeAverageResponse(
        columnName = columnName,
        averageTime = averageTime
    )
