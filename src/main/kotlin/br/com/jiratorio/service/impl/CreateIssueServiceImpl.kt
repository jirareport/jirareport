package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.log
import br.com.jiratorio.parser.IssueParser
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.CreateIssueService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.WipService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.leadtime.LeadTimeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CreateIssueServiceImpl(
    private val issueRepository: IssueRepository,
    private val issueClient: IssueClient,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val issueService: IssueService,
    private val boardService: BoardService,
    private val chartService: ChartService,
    private val jqlService: JQLService,
    private val wipService: WipService,
    private val issueParser: IssueParser,
    private val leadTimeService: LeadTimeService
) : CreateIssueService {

    @Transactional
    @ExecutionTime
    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Method=create, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)

        val (startDate, endDate) = createIssuePeriodRequest

        delete(startDate, endDate, boardId)

        val board = boardService.findById(boardId)

        val jql = jqlService.finalizedIssues(board, startDate, endDate)

        val issues = buildIssues(jql, board)

        val leadTime = issues.map { it.leadTime }.average().zeroIfNaN()
        val avgPctEfficiency = issues.map { it.pctEfficiency }.average().zeroIfNaN()

        val fluxColumn = FluxColumn(board)
        val wipAvg = wipService.calcAvgWip(startDate, endDate, issues, fluxColumn.wipColumns)

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

        leadTimeService.createLeadTimes(issues, board.id)

        createCharts(issues, board, issuePeriod)

        return issuePeriod.id
    }

    private fun createCharts(
        issues: List<Issue>,
        board: Board,
        issuePeriod: IssuePeriod
    ) {
        val chartAggregator = chartService.buildAllCharts(issues, board)

        issuePeriod.run {
            histogram = chartAggregator.histogram
            leadTimeByEstimate = chartAggregator.throughputByEstimate
            throughputByEstimate = chartAggregator.leadTimeByEstimate
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

    private fun buildIssues(
        jql: String,
        board: Board
    ): List<Issue> {
        val issuesStr = issueClient.findByJql(jql)
        val issues = issueParser.parse(issuesStr, board)
        return issues
    }

    private fun delete(startDate: LocalDate, endDate: LocalDate, boardId: Long) {
        log.info("Method=delete, startDate={}, endDate={}, boardId={}", startDate, endDate, boardId)

        val issuePeriod = issuePeriodRepository.findByStartDateAndEndDateAndBoardId(startDate, endDate, boardId)
        if (issuePeriod != null) {
            issuePeriodRepository.delete(issuePeriod)
            issueService.deleteAll(issuePeriod.issues)
        }
    }

}
