package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.response.LeadTimeResponse
import br.com.jiratorio.extension.time.displayFormat

fun LeadTime.toLeadTimeResponse(): LeadTimeResponse =
    LeadTimeResponse(
        name = leadTimeConfig.name,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<LeadTime>.toLeadTimeResponse(): Set<LeadTimeResponse> =
    map { it.toLeadTimeResponse() }.toSet()
