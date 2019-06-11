package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.response.ChangelogResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun Changelog.toChangelogResponse(): ChangelogResponse =
    ChangelogResponse(
        from = from,
        to = to,
        startDate = created.format(dateTimePattern),
        endDate = endDate.format(dateTimePattern),
        leadTime = leadTime
    )

fun List<Changelog>.toChangelogResponse(): List<ChangelogResponse> =
    map { it.toChangelogResponse() }
