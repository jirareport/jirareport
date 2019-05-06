package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.response.LeadTimeResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class LeadTimeMapper {

    fun leadTimeToLeadTimeResponse(leadTime: LeadTime): LeadTimeResponse {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return LeadTimeResponse(
            name = leadTime.leadTimeConfig.name,
            startDate = leadTime.startDate.format(formatter),
            endDate = leadTime.endDate.format(formatter),
            leadTime = leadTime.leadTime
        )
    }

    fun leadTimeToLeadTimeResponse(leadTimes: Set<LeadTime>?): Set<LeadTimeResponse>? {
        return leadTimes?.map { leadTimeToLeadTimeResponse(it) }?.toSet()
    }

}
