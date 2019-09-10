package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.config.internationalization.MessageResolver
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
    private val dynamicChartService: DynamicChartService,
    private val messageResolver: MessageResolver
) : ChartService {

    @ExecutionTime
    override fun buildAllCharts(issues: List<Issue>, board: Board): ChartAggregator {
        log.info("Method=buildAllCharts, issues={}, board={}", issues, board)
        val uninformed = messageResolver.resolve("uninformed")

        val histogram = histogramService.issueHistogram(issues)
        val leadTimeByEstimate = estimateChartService.leadTimeChart(issues, uninformed)
        val throughputByEstimate = estimateChartService.throughputChart(issues, uninformed)
        val leadTimeBySystem = systemChartService.leadTimeBySystem(issues, uninformed)
        val throughputBySystem = systemChartService.throughputBySystem(issues, uninformed)
        val leadTimeByType = typeChartService.leadTimeByType(issues, uninformed)
        val leadTimeByProject = projectChartService.leadTimeByProject(issues, uninformed)
        val throughputByType = typeChartService.throughputByType(issues, uninformed)
        val throughputByProject = projectChartService.throughputByProject(issues, uninformed)
        val leadTimeByPriority = priorityChartService.leadTimeByPriority(issues, uninformed)
        val throughputByPriority = priorityChartService.throughputByPriority(issues, uninformed)
        val leadTimeCompareChart = leadTimeCompareChartService.leadTimeCompare(issues)
        val columnTimeAvg = columnTimeChartService.average(issues, board.fluxColumn ?: emptyList())
        val dynamicCharts = dynamicChartService.buildDynamicCharts(issues, board, uninformed)

        return ChartAggregator(
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
            leadTimeCompareChart = leadTimeCompareChart,
            columnTimeAvg = columnTimeAvg,
            dynamicCharts = dynamicCharts
        )
    }
}
