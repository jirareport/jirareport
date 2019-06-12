package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.response.ChangelogResponse
import br.com.jiratorio.extension.time.displayFormat

fun Changelog.toChangelogResponse(): ChangelogResponse =
    ChangelogResponse(
        from = from,
        to = to,
        startDate = created.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun List<Changelog>.toChangelogResponse(): List<ChangelogResponse> =
    map { it.toChangelogResponse() }
