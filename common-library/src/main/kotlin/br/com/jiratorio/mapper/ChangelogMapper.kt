package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.response.ChangelogResponse
import br.com.jiratorio.extension.time.displayFormat

fun ColumnChangelogEntity.toChangelogResponse(): ChangelogResponse =
    ChangelogResponse(
        from = from,
        to = to,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<ColumnChangelogEntity>.toChangelogResponse(): List<ChangelogResponse> =
    map { it.toChangelogResponse() }
