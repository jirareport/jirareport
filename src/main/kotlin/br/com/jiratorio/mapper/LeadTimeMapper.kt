package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.response.LeadTimeResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class LeadTimeMapper {

    private val dateTimePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    fun leadTimeToLeadTimeResponse(leadTime: LeadTime): LeadTimeResponse {
        return LeadTimeResponse(
            name = leadTime.leadTimeConfig.name,
            startDate = leadTime.startDate.format(dateTimePattern),
            endDate = leadTime.endDate.format(dateTimePattern),
            leadTime = leadTime.leadTime
        )
    }

    fun leadTimeToLeadTimeResponse(leadTimes: Set<LeadTime>?): Set<LeadTimeResponse>? {
        return leadTimes?.map { leadTimeToLeadTimeResponse(it) }?.toSet()
    }

}
