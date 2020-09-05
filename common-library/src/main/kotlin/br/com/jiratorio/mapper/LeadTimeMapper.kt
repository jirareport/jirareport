package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.response.LeadTimeResponse
import br.com.jiratorio.extension.time.displayFormat

fun LeadTimeEntity.toLeadTimeResponse(): LeadTimeResponse =
    LeadTimeResponse(
        name = leadTimeConfig.name,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<LeadTimeEntity>.toLeadTimeResponse(): Set<LeadTimeResponse> =
    map { it.toLeadTimeResponse() }.toSet()
