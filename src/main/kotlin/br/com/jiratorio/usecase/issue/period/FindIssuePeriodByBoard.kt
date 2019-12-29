package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByBoardResponse
import br.com.jiratorio.extension.decimal.format
import br.com.jiratorio.mapper.toIssuePeriodResponse
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.usecase.board.FindBoard
import br.com.jiratorio.usecase.chart.issue.period.CreateLeadTimeCompareChartByPeriod
import br.com.jiratorio.usecase.chart.issue.period.CreateThroughputByEstimateChart
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssuePeriodByBoard(
    private val findBoard: FindBoard,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val jiraProperties: JiraProperties,
    private val createLeadTimeCompareChartByPeriod: CreateLeadTimeCompareChartByPeriod,
    private val createThroughputByEstimateChart: CreateThroughputByEstimateChart
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long): IssuePeriodByBoardResponse {
        log.info("Method=execute, boardId={}", boardId)

        val board = findBoard.execute(boardId)

        val issuePeriods = issuePeriodRepository.findByBoardId(boardId)
            .sortedBy { it.startDate }

        return IssuePeriodByBoardResponse(
            periods = issuePeriods.toIssuePeriodResponse(jiraProperties.url),
            charts = buildCharts(issuePeriods, board)
        )
    }

    private fun buildCharts(issuePeriods: List<IssuePeriod>, board: Board): IssuePeriodChartResponse {
        log.info("Method=buildCharts, issuePeriods={}, board={}", issuePeriods, board)

        val leadTime: Chart<String, String> = Chart()
        val throughput: Chart<String, Int> = Chart()

        for (issuePeriod in issuePeriods) {
            leadTime[issuePeriod.dates] = issuePeriod.leadTime.format()
            throughput[issuePeriod.dates] = issuePeriod.throughput
        }

        return IssuePeriodChartResponse(
            throughputByEstimate = createThroughputByEstimateChart.execute(issuePeriods),
            leadTimeCompareChart = createLeadTimeCompareChartByPeriod.execute(issuePeriods, board),
            leadTime = leadTime,
            throughput = throughput
        )
    }

}
