package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.usecase.chart.CreateChartAggregator
import br.com.jiratorio.usecase.impediment.history.CreateImpedimentHistory
import br.com.jiratorio.usecase.jql.CreateFinalizedIssueJql
import br.com.jiratorio.usecase.leadtime.CreateLeadTime
import br.com.jiratorio.usecase.parse.ParseIssue
import br.com.jiratorio.usecase.wip.CalculateAverageWip
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateIssuePeriod(
    private val issueRepository: IssueRepository,
    private val issueClient: IssueClient,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val boardRepository: BoardRepository,
    private val createChartAggregator: CreateChartAggregator,
    private val createFinalizedIssueJql: CreateFinalizedIssueJql,
    private val parseIssue: ParseIssue,
    private val createLeadTime: CreateLeadTime,
    private val createImpedimentHistory: CreateImpedimentHistory,
    private val calculateAverageWip: CalculateAverageWip
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Method=execute, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)

        val (startDate, endDate) = createIssuePeriodRequest

        issuePeriodRepository.deleteByStartDateAndEndDateAndBoardId(
            createIssuePeriodRequest.startDate,
            createIssuePeriodRequest.endDate,
            boardId
        )

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        val jql = createFinalizedIssueJql.execute(board, startDate, endDate)

        val issues = parseIssue.execute(
            root = issueClient.findByJql(jql),
            board = board
        )

        val leadTime = issues.map { it.leadTime }.average().zeroIfNaN()
        val avgPctEfficiency = issues.map { it.pctEfficiency }.average().zeroIfNaN()

        val fluxColumn = FluxColumn(board)
        val wipAvg = calculateAverageWip.execute(startDate, endDate, issues, fluxColumn.wipColumns)

        val issuePeriod = IssuePeriod(
            startDate = startDate,
            endDate = endDate,
            boardId = boardId,
            issues = issues.toMutableSet(),
            leadTime = leadTime,
            throughput = issues.size,
            jql = jql,
            wipAvg = wipAvg,
            avgPctEfficiency = avgPctEfficiency
        )

        issuePeriodRepository.save(issuePeriod)

        issues.forEach {
            it.issuePeriodId = issuePeriod.id
        }

        issueRepository.saveAll(issues)

        createLeadTime.execute(issues, board.id)
        createImpedimentHistory.execute(issues)

        createCharts(issues, board, issuePeriod)

        return issuePeriod.id
    }

    private fun createCharts(
        issues: List<Issue>,
        board: Board,
        issuePeriod: IssuePeriod
    ) {
        val chartAggregator = createChartAggregator.execute(issues, board)

        issuePeriod.run {
            histogram = chartAggregator.histogram
            leadTimeByEstimate = chartAggregator.leadTimeByEstimate
            throughputByEstimate = chartAggregator.throughputByEstimate
            leadTimeBySystem = chartAggregator.leadTimeBySystem
            throughputBySystem = chartAggregator.throughputBySystem
            columnTimeAvg = chartAggregator.columnTimeAvg.toMutableList()
            leadTimeByType = chartAggregator.leadTimeByType
            throughputByType = chartAggregator.throughputByType
            leadTimeByProject = chartAggregator.leadTimeByProject
            throughputByProject = chartAggregator.throughputByProject
            leadTimeCompareChart = chartAggregator.leadTimeCompareChart
            leadTimeByPriority = chartAggregator.leadTimeByPriority
            throughputByPriority = chartAggregator.throughputByPriority
            dynamicCharts = chartAggregator.dynamicCharts.toMutableList()
            leadTimeCompareChart = chartAggregator.leadTimeCompareChart
        }

        issuePeriodRepository.save(issuePeriod)
    }

}
