package br.com.jiratorio.usecase.chart

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.usecase.chart.dynamic.CreateDynamicChartUseCase
import br.com.jiratorio.usecase.chart.estimate.CreateEstimateLeadTimeChartUseCase
import br.com.jiratorio.usecase.chart.estimate.CreateEstimateThroughputChartUseCase
import br.com.jiratorio.usecase.chart.histogram.CreateHistogramChartUseCase
import br.com.jiratorio.usecase.chart.issue.CreateIssueProgressionChartUseCase
import br.com.jiratorio.usecase.chart.issue.type.CreateIssueTypeLeadTimeChartUseCase
import br.com.jiratorio.usecase.chart.issue.type.CreateIssueTypeThroughputChartUseCase
import br.com.jiratorio.usecase.chart.leadtimecompare.CreateLeadTimeCompareChartUseCase
import br.com.jiratorio.usecase.chart.priority.CreatePriorityLeadTimeChartUseCase
import br.com.jiratorio.usecase.chart.priority.CreatePriorityThroughputChartUseCase
import br.com.jiratorio.usecase.chart.project.CreateProjectLeadTimeChartUseCase
import br.com.jiratorio.usecase.chart.project.CreateProjectThroughputChartUseCase
import br.com.jiratorio.usecase.chart.system.CreateSystemLeadTimeChartUseCase
import br.com.jiratorio.usecase.chart.system.CreateSystemThroughputChartUseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateChartAggregatorUseCase(
    private val createDynamicChart: CreateDynamicChartUseCase,
    private val createEstimateLeadTimeChart: CreateEstimateLeadTimeChartUseCase,
    private val createEstimateThroughputChart: CreateEstimateThroughputChartUseCase,
    private val createHistogramChart: CreateHistogramChartUseCase,
    private val createIssueTypeLeadTimeChart: CreateIssueTypeLeadTimeChartUseCase,
    private val createIssueTypeThroughputChart: CreateIssueTypeThroughputChartUseCase,
    private val createLeadTimeCompareChart: CreateLeadTimeCompareChartUseCase,
    private val createPriorityLeadTimeChart: CreatePriorityLeadTimeChartUseCase,
    private val createPriorityThroughputChart: CreatePriorityThroughputChartUseCase,
    private val createProjectLeadTimeChart: CreateProjectLeadTimeChartUseCase,
    private val createProjectThroughputChart: CreateProjectThroughputChartUseCase,
    private val createSystemLeadTimeChart: CreateSystemLeadTimeChartUseCase,
    private val createSystemThroughputChart: CreateSystemThroughputChartUseCase,
    private val createIssueProgressionChart: CreateIssueProgressionChartUseCase,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>, board: BoardEntity): ChartAggregator {
        log.info("Action=createChartAggregator, issues={}, board={}", issues, board)

        return ChartAggregator(
            histogram = createHistogramChart.execute(issues),
            leadTimeByEstimate = createEstimateLeadTimeChart.execute(issues),
            throughputByEstimate = createEstimateThroughputChart.execute(issues),
            leadTimeBySystem = createSystemLeadTimeChart.execute(issues),
            throughputBySystem = createSystemThroughputChart.execute(issues),
            leadTimeByType = createIssueTypeLeadTimeChart.execute(issues),
            throughputByType = createIssueTypeThroughputChart.execute(issues),
            leadTimeByProject = createProjectLeadTimeChart.execute(issues),
            throughputByProject = createProjectThroughputChart.execute(issues),
            leadTimeByPriority = createPriorityLeadTimeChart.execute(issues),
            throughputByPriority = createPriorityThroughputChart.execute(issues),
            leadTimeCompareChart = createLeadTimeCompareChart.execute(issues),
            dynamicCharts = createDynamicChart.execute(board, issues),
            issueProgression = createIssueProgressionChart.execute(board, issues)
        )
    }

}
