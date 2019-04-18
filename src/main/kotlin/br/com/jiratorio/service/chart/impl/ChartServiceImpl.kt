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
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.runBlocking
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
    private val chartCoroutineDispatcher: ExecutorCoroutineDispatcher,
    private val messageResolver: MessageResolver
) : ChartService {

    @ExecutionTime
    override fun buildAllCharts(issues: List<Issue>, board: Board): ChartAggregator {
        log.info("Method=buildAllCharts, issues={}, board={}", issues, board)
        val uninformed = messageResolver.resolve("uninformed")

        return runBlocking(chartCoroutineDispatcher) {
            val histogram = histogramService.issueHistogramAsync(issues)
            val leadTimeByEstimate = estimateChartService.throughputChartAsync(issues, uninformed)
            val throughputByEstimate = estimateChartService.leadTimeChartAsync(issues, uninformed)
            val leadTimeBySystem = systemChartService.leadTimeBySystemAsync(issues, uninformed)
            val throughputBySystem = systemChartService.throughputBySystemAsync(issues, uninformed)
            val leadTimeByType = typeChartService.leadTimeByTypeAsync(issues, uninformed)
            val throughputByType = typeChartService.throughputByTypeAsync(issues, uninformed)
            val leadTimeByProject = projectChartService.leadTimeByProjectAsync(issues, uninformed)
            val throughputByProject = projectChartService.throughputByProjectAsync(issues, uninformed)
            val leadTimeByPriority = priorityChartService.leadTimeByPriorityAsync(issues, uninformed)
            val throughputByPriority = priorityChartService.throughputByPriorityAsync(issues, uninformed)
            val leadTimeCompareChart = leadTimeCompareChartService.leadTimeCompareAsync(issues)
            val columnTimeAvg = columnTimeChartService.averageAsync(issues, board.fluxColumn ?: emptyList())
            val dynamicCharts = dynamicChartService.buildDynamicChartsAsync(issues, board, uninformed)

            ChartAggregator(
                histogram = histogram.await(),
                leadTimeByEstimate = leadTimeByEstimate.await(),
                throughputByEstimate = throughputByEstimate.await(),
                leadTimeBySystem = leadTimeBySystem.await(),
                throughputBySystem = throughputBySystem.await(),
                leadTimeByType = leadTimeByType.await(),
                throughputByType = throughputByType.await(),
                leadTimeByProject = leadTimeByProject.await(),
                throughputByProject = throughputByProject.await(),
                leadTimeByPriority = leadTimeByPriority.await(),
                throughputByPriority = throughputByPriority.await(),
                leadTimeCompareChart = leadTimeCompareChart.await(),
                columnTimeAvg = columnTimeAvg.await(),
                dynamicCharts = dynamicCharts.await()
            )
        }
    }

}