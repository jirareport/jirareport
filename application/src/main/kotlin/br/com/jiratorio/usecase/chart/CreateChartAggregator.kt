package br.com.jiratorio.usecase.chart

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.usecase.chart.dynamic.CreateDynamicChart
import br.com.jiratorio.usecase.chart.estimate.CreateEstimateLeadTimeChart
import br.com.jiratorio.usecase.chart.estimate.CreateEstimateThroughputChart
import br.com.jiratorio.usecase.chart.histogram.CreateHistogramChart
import br.com.jiratorio.usecase.chart.issue.CreateIssueProgressionChart
import br.com.jiratorio.usecase.chart.issue.type.CreateIssueTypeLeadTimeChart
import br.com.jiratorio.usecase.chart.issue.type.CreateIssueTypeThroughputChart
import br.com.jiratorio.usecase.chart.leadtimecompare.CreateLeadTimeCompareChart
import br.com.jiratorio.usecase.chart.priority.CreatePriorityLeadTimeChart
import br.com.jiratorio.usecase.chart.priority.CreatePriorityThroughputChart
import br.com.jiratorio.usecase.chart.project.CreateProjectLeadTimeChart
import br.com.jiratorio.usecase.chart.project.CreateProjectThroughputChart
import br.com.jiratorio.usecase.chart.system.CreateSystemLeadTimeChart
import br.com.jiratorio.usecase.chart.system.CreateSystemThroughputChart
import org.slf4j.LoggerFactory

@UseCase
class CreateChartAggregator(
    private val createDynamicChart: CreateDynamicChart,
    private val createEstimateLeadTimeChart: CreateEstimateLeadTimeChart,
    private val createEstimateThroughputChart: CreateEstimateThroughputChart,
    private val createHistogramChart: CreateHistogramChart,
    private val createIssueTypeLeadTimeChart: CreateIssueTypeLeadTimeChart,
    private val createIssueTypeThroughputChart: CreateIssueTypeThroughputChart,
    private val createLeadTimeCompareChart: CreateLeadTimeCompareChart,
    private val createPriorityLeadTimeChart: CreatePriorityLeadTimeChart,
    private val createPriorityThroughputChart: CreatePriorityThroughputChart,
    private val createProjectLeadTimeChart: CreateProjectLeadTimeChart,
    private val createProjectThroughputChart: CreateProjectThroughputChart,
    private val createSystemLeadTimeChart: CreateSystemLeadTimeChart,
    private val createSystemThroughputChart: CreateSystemThroughputChart,
    private val createIssueProgressionChart: CreateIssueProgressionChart
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>, board: Board): ChartAggregator {
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
