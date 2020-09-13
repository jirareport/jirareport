package br.com.jiratorio.mapper

import br.com.jiratorio.domain.changelog.ColumnChangelog
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.response.ChangelogResponse
import br.com.jiratorio.extension.time.displayFormat

fun ColumnChangelog.toChangelogResponse(): ChangelogResponse =
    ChangelogResponse(
        from = from,
        to = to,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<ColumnChangelog>.toChangelogResponse(): List<ChangelogResponse> =
    map { it.toChangelogResponse() }

fun Set<ColumnChangelog>.toColumnChangelogEntity(): Set<ColumnChangelogEntity> =
    map { it.toColumnChangelogEntity() }.toSet()

fun ColumnChangelog.toColumnChangelogEntity(): ColumnChangelogEntity =
    ColumnChangelogEntity(
        from = from,
        to = to,
        startDate = startDate,
        endDate = endDate,
        leadTime = leadTime
    )
