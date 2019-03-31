package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.response.IssuePeriodResponse
import org.springframework.stereotype.Component

@Component
class IssuePeriodMapper {

    fun issuePeriodToIssuePeriodResponse(issuePeriods: List<IssuePeriod>): List<IssuePeriodResponse> {
        return issuePeriods.map { issuePeriodToIssuePeriodResponse(it) }
    }

    fun issuePeriodToIssuePeriodResponse(issuePeriod: IssuePeriod): IssuePeriodResponse {
        return IssuePeriodResponse(
            id = issuePeriod.id,
            dates = issuePeriod.dates,
            wipAvg = issuePeriod.wipAvg,
            leadTime = issuePeriod.avgLeadTime,
            avgPctEfficiency = issuePeriod.avgPctEfficiency,
            jql = issuePeriod.jql,
            issuesCount = issuePeriod.issuesCount
        )
    }

}
