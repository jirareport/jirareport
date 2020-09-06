package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodListResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.format
import br.com.jiratorio.mapper.toIssuePeriodResponse
import br.com.jiratorio.property.JiraProperties
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.usecase.chart.issue.period.CreateIssueTypePerformanceCompareChartUseCase
import br.com.jiratorio.usecase.chart.issue.period.CreateLeadTimeCompareChartByPeriodUseCase
import br.com.jiratorio.usecase.chart.issue.period.CreateThroughputByEstimateChartUseCase
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllIssuePeriodsUseCase(
    private val boardRepository: BoardRepository,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val jiraProperties: JiraProperties,
    private val createLeadTimeCompareChartByPeriod: CreateLeadTimeCompareChartByPeriodUseCase,
    private val createThroughputByEstimateChart: CreateThroughputByEstimateChartUseCase,
    private val createIssueTypePerformanceCompareChart: CreateIssueTypePerformanceCompareChartUseCase,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(filter: FindAllIssuePeriodsFilter): IssuePeriodListResponse {
        log.info("Action=findAllIssuePeriods, findAllIssuePeriodsFilter={}", filter)

        val boardPreferences = boardRepository.findIssuePeriodPreferencesByBoard(filter.boardId) ?: throw ResourceNotFound()

        val issuePeriods = issuePeriodRepository.findAll(filter, boardPreferences)

        return IssuePeriodListResponse(
            periods = issuePeriods.toIssuePeriodResponse(jiraProperties.url),
            charts = buildCharts(issuePeriods, filter, boardPreferences)
        )
    }

    private fun buildCharts(issuePeriods: List<MinimalIssuePeriod>, filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): IssuePeriodChartResponse {
        log.info("Method=buildCharts, issuePeriods={}, filter={},  boardPreferences={}", issuePeriods, filter, boardPreferences)

        val leadTime: Chart<String, String> = Chart()
        val throughput: Chart<String, Int> = Chart()

        for (issuePeriod in issuePeriods) {
            leadTime[issuePeriod.name] = issuePeriod.leadTime.format()
            throughput[issuePeriod.name] = issuePeriod.throughput
        }

        return IssuePeriodChartResponse(
            leadTime = leadTime,
            throughput = throughput,
            leadTimeCompareChart = createLeadTimeCompareChartByPeriod.execute(filter, boardPreferences),
            throughputByEstimate = createThroughputByEstimateChart.execute(filter, boardPreferences),
            issueTypePerformanceCompareChart = createIssueTypePerformanceCompareChart.execute(filter, boardPreferences)
        )
    }

}
