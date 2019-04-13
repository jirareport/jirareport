package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodResponse
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
            leadTime = issuePeriod.leadTime,
            avgPctEfficiency = issuePeriod.avgPctEfficiency,
            jql = issuePeriod.jql,
            issuesCount = issuePeriod.issuesCount
        )
    }

    fun issuePeriodToIssuePeriodDetailResponse(
        issuePeriod: IssuePeriod
    ): IssuePeriodDetailResponse {
        return IssuePeriodDetailResponse(
            dates = issuePeriod.dates,
            leadTime = issuePeriod.leadTime,
            issuesCount = issuePeriod.issuesCount,
            leadTimeByEstimate = issuePeriod.leadTimeByEstimate,
            throughputByEstimate = issuePeriod.throughputByEstimate,
            leadTimeBySystem = issuePeriod.leadTimeBySystem,
            throughputBySystem = issuePeriod.throughputBySystem,
            leadTimeByType = issuePeriod.leadTimeByType,
            throughputByType = issuePeriod.throughputByType,
            leadTimeByProject = issuePeriod.leadTimeByProject,
            throughputByProject = issuePeriod.throughputByProject,
            leadTimeByPriority = issuePeriod.leadTimeByPriority,
            throughputByPriority = issuePeriod.throughputByPriority,
            columnTimeAvg = issuePeriod.columnTimeAvg,
            leadTimeCompareChart = issuePeriod.leadTimeCompareChart,
            dynamicCharts = issuePeriod.dynamicCharts
        )
    }

}
