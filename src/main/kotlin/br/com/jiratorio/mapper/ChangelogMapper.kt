package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.response.ChangelogResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun Changelog.toChangelogResponse(): ChangelogResponse =
    ChangelogResponse(
        from = this.from,
        to = this.to,
        startDate = this.created.format(dateTimePattern),
        endDate = this.endDate.format(dateTimePattern),
        leadTime = this.leadTime
    )

fun List<Changelog>.toChangelogResponse(): List<ChangelogResponse> =
    this.map { it.toChangelogResponse() }
