package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodResponse

fun List<IssuePeriod>.toIssuePeriodResponse(jiraUrl: String): List<IssuePeriodResponse> =
    map { it.toIssuePeriodResponse(jiraUrl) }

fun IssuePeriod.toIssuePeriodResponse(jiraUrl: String): IssuePeriodResponse =
    IssuePeriodResponse(
        id = id,
        dates = dates,
        wipAvg = wipAvg,
        leadTime = leadTime,
        avgPctEfficiency = avgPctEfficiency,
        jql = jql,
        throughput = throughput,
        detailsUrl = "$jiraUrl/issues/?jql=$jql"
    )

fun IssuePeriod.toIssuePeriodDetailResponse(): IssuePeriodDetailResponse =
    IssuePeriodDetailResponse(
        dates = dates,
        leadTime = leadTime,
        throughput = throughput,
        histogram = histogram,
        leadTimeByEstimate = leadTimeByEstimate,
        throughputByEstimate = throughputByEstimate,
        leadTimeBySystem = leadTimeBySystem,
        throughputBySystem = throughputBySystem,
        leadTimeByType = leadTimeByType,
        throughputByType = throughputByType,
        leadTimeByProject = leadTimeByProject,
        throughputByProject = throughputByProject,
        leadTimeByPriority = leadTimeByPriority,
        throughputByPriority = throughputByPriority,
        columnTimeAverages = columnTimeAverages.toResponse(),
        leadTimeCompareChart = leadTimeCompareChart,
        dynamicCharts = dynamicCharts,
        issueProgression = issueProgression
    )
