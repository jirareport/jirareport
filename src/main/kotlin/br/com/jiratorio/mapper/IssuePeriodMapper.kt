package br.com.jiratorio.mapper

import br.com.jiratorio.domain.chart.ChartAggregator
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

fun IssuePeriodEntity.toIssuePeriodDetailResponse(charts: ChartAggregator): IssuePeriodDetailResponse =
    IssuePeriodDetailResponse(
        name = name,
        leadTime = leadTime,
        throughput = throughput,
        histogram = charts.histogram,
        leadTimeByEstimate = charts.leadTimeByEstimate,
        throughputByEstimate = charts.throughputByEstimate,
        leadTimeBySystem = charts.leadTimeBySystem,
        throughputBySystem = charts.throughputBySystem,
        leadTimeByType = charts.leadTimeByType,
        throughputByType = charts.throughputByType,
        leadTimeByProject = charts.leadTimeByProject,
        throughputByProject = charts.throughputByProject,
        leadTimeByPriority = charts.leadTimeByPriority,
        throughputByPriority = charts.throughputByPriority,
        columnTimeAverages = columnTimeAverages.toResponse(),
        leadTimeCompareChart = charts.leadTimeCompareChart,
        dynamicCharts = charts.dynamicCharts.toMutableList(),
        issueProgression = charts.issueProgression
    )
