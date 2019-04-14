package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.chart.ColumnTimeChartService
import br.com.jiratorio.service.chart.DynamicChartService
import br.com.jiratorio.service.chart.EstimateChartService
import br.com.jiratorio.service.chart.HistogramService
import br.com.jiratorio.service.chart.IssueTypeChartService
import br.com.jiratorio.service.chart.LeadTimeCompareChartService
import br.com.jiratorio.service.chart.PriorityChartService
import br.com.jiratorio.service.chart.ProjectChartService
import br.com.jiratorio.service.chart.SystemChartService
import org.springframework.stereotype.Service

@Service
class ChartServiceImpl(
    private val histogramService: HistogramService,
    private val estimateChartService: EstimateChartService,
    private val systemChartService: SystemChartService,
    private val typeChartService: IssueTypeChartService,
    private val projectChartService: ProjectChartService,
    private val priorityChartService: PriorityChartService,
    private val leadTimeCompareChartService: LeadTimeCompareChartService,
    private val columnTimeChartService: ColumnTimeChartService,
    private val dynamicChartService: DynamicChartService
) : ChartService {

    @ExecutionTime
    override fun buildAllCharts(issues: List<Issue>, board: Board): ChartAggregator {
        log.info("Method=buildAllCharts, issues={}, board={}", issues, board)

        return ChartAggregator(
            histogram = histogramService.issueHistogram(issues),
            leadTimeByEstimate = estimateChartService.throughputChart(issues),
            throughputByEstimate = estimateChartService.leadTimeChart(issues),
            leadTimeBySystem = systemChartService.leadTimeBySystem(issues),
            throughputBySystem = systemChartService.throughputBySystem(issues),
            leadTimeByType = typeChartService.leadTimeByType(issues),
            throughputByType = typeChartService.throughputByType(issues),
            leadTimeByProject = projectChartService.leadTimeByProject(issues),
            throughputByProject = projectChartService.throughputByProject(issues),
            leadTimeByPriority = priorityChartService.leadTimeByPriority(issues),
            throughputByPriority = priorityChartService.throughputByPriority(issues),
            leadTimeCompareChart = leadTimeCompareChartService.leadTimeCompare(issues),
            columnTimeAvg = columnTimeChartService.average(issues, board.fluxColumn ?: emptyList()),
            dynamicCharts = dynamicChartService.buildDynamicCharts(issues, board)
        )
    }

}
