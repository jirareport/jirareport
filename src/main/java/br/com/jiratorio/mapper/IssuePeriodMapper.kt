package br.com.jiratorio.mapper

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.IssuePeriodDetails
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import org.springframework.stereotype.Component

@Component
class IssuePeriodMapper {

    fun fromJiraData(
        createIssuePeriodRequest: CreateIssuePeriodRequest,
        issues: MutableList<Issue>,
        chartAggregator: ChartAggregator,
        details: IssuePeriodDetails
    ) = IssuePeriod(
        startDate = createIssuePeriodRequest.startDate,
        endDate = createIssuePeriodRequest.endDate,
        boardId = details.boardId,
        issues = issues,
        avgLeadTime = details.avgLeadTime,
        histogram = chartAggregator.histogram,
        estimated = chartAggregator.estimated,
        leadTimeBySystem = chartAggregator.leadTimeBySystem,
        tasksBySystem = chartAggregator.tasksBySystem,
        leadTimeBySize = chartAggregator.leadTimeBySize,
        columnTimeAvgs = chartAggregator.columnTimeAvg,
        leadTimeByType = chartAggregator.leadTimeByType,
        tasksByType = chartAggregator.tasksByType,
        leadTimeByProject = chartAggregator.leadTimeByProject,
        tasksByProject = chartAggregator.tasksByProject,
        leadTimeCompareChart = chartAggregator.leadTimeCompareChart,
        leadTimeByPriority = chartAggregator.leadTimeByPriority,
        throughputByPriority = chartAggregator.throughputByPriority,
        dynamicCharts = chartAggregator.dynamicCharts,
        issuesCount = details.issueCount,
        jql = details.jql,
        wipAvg = details.wipAvg,
        avgPctEfficiency = details.avgPctEfficiency
    )

}
