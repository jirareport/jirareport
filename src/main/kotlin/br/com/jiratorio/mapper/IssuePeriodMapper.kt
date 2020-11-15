package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodResponse

fun List<MinimalIssuePeriod>.toIssuePeriodResponse(jiraUrl: String): List<IssuePeriodResponse> =
    map { it.toIssuePeriodResponse(jiraUrl) }

fun MinimalIssuePeriod.toIssuePeriodResponse(jiraUrl: String): IssuePeriodResponse =
    IssuePeriodResponse(
        id = id,
        name = name,
        wipAvg = wipAvg,
        leadTime = leadTime,
        avgPctEfficiency = avgPctEfficiency,
        jql = jql,
        throughput = throughput,
        detailsUrl = "$jiraUrl/issues/?jql=$jql"
    )

fun IssuePeriodEntity.toIssuePeriodDetailResponse(): IssuePeriodDetailResponse =
    IssuePeriodDetailResponse(
        name = name,
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
