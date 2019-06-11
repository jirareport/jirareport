package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.response.LeadTimeResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun LeadTime.toLeadTimeResponse(): LeadTimeResponse =
    LeadTimeResponse(
        name = leadTimeConfig.name,
        startDate = startDate.format(dateTimePattern),
        endDate = endDate.format(dateTimePattern),
        leadTime = leadTime
    )

fun Set<LeadTime>.toLeadTimeResponse(): Set<LeadTimeResponse> =
    map { it.toLeadTimeResponse() }.toSet()
