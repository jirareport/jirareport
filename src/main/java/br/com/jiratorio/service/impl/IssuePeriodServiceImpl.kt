package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.IssuePeriodResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.logger
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.ChartService
import br.com.jiratorio.service.IssuePeriodService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.WipService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class IssuePeriodServiceImpl(
    private val issueService: IssueService,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val chartService: ChartService,
    private val boardService: BoardService,
    private val wipService: WipService,
    private val jqlService: JQLService
) : IssuePeriodService {

    private val log = logger()

    @Transactional
    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Method=create, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)

        val (startDate, endDate) = createIssuePeriodRequest

        delete(startDate, endDate, boardId)

        val board = boardService.findById(boardId)

        val jql = jqlService.finalizedIssues(board, startDate, endDate)
        val issues = issueService.createByJql(jql, board)

        val avgLeadTime = issues
            .map { it.leadTime }
            .average()

        val avgPctEfficiency = issues
            .map { it.pctEfficiency }
            .average()

        val chartAggregator = chartService.buildAllCharts(issues, board)

        val fluxColumn = FluxColumn(board)
        val wipAvg = wipService.calcAvgWip(startDate, endDate, issues, fluxColumn.wipColumns)

        val issuePeriod = IssuePeriod(
            startDate = startDate,
            endDate = endDate,
            boardId = boardId,
            issues = issues,
            avgLeadTime = avgLeadTime,
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
            issuesCount = issues.size,
            jql = jql,
            wipAvg = wipAvg,
            avgPctEfficiency = avgPctEfficiency
        )

        issuePeriodRepository.save(issuePeriod)

        return issuePeriod.id
    }

    private fun delete(startDate: LocalDate, endDate: LocalDate, boardId: Long) {
        log.info("Method=delete, startDate={}, endDate={}, boardId={}", startDate, endDate, boardId)

        issuePeriodRepository.findByStartDateAndEndDateAndBoardId(startDate, endDate, boardId)
            .ifPresent { delete(it) }
    }

    private fun delete(issuePeriod: IssuePeriod) {
        issuePeriodRepository.delete(issuePeriod)
        issueService.deleteAll(issuePeriod.issues)
    }

    @Transactional
    override fun remove(id: Long) {
        log.info("Method=remove, id={}", id)

        val issuePeriod = findById(id)
        delete(issuePeriod)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): IssuePeriod {
        log.info("Method=findById, id={}", id)

        return issuePeriodRepository.findById(id)
            .orElseThrow(::ResourceNotFound)
    }

    @Transactional
    override fun update(id: Long) {
        log.info("Method=update, id={}", id)

        val issuePeriod = findById(id)
        val createIssuePeriodRequest = CreateIssuePeriodRequest(issuePeriod.startDate, issuePeriod.endDate)

        create(createIssuePeriodRequest, issuePeriod.boardId)
    }

    @Transactional(readOnly = true)
    override fun findIssuePeriodByBoard(boardId: Long): IssuePeriodResponse {
        log.info("Method=findIssuePeriodByBoard, boardId={}", boardId)

        val board = boardService.findById(boardId)

        val issuePeriods = issuePeriodRepository.findByBoardId(boardId)
            .sortedBy { it.startDate }

        val issuePeriodChart = buildCharts(issuePeriods, board)

        return IssuePeriodResponse(issuePeriods, issuePeriodChart)
    }

    private fun buildCharts(issuePeriods: List<IssuePeriod>, board: Board): IssuePeriodChartResponse {
        log.info("Method=buildCharts, issuePeriods={}, board={}", issuePeriods, board)

        val leadTime: Chart<String, String> = Chart()
        val issuesCount: Chart<String, Int> = Chart()

        for (issuePeriod in issuePeriods) {
            leadTime[issuePeriod.dates] = "%.2f".format(issuePeriod.avgLeadTime)
            issuesCount[issuePeriod.dates] = issuePeriod.issuesCount
        }

        val issueCountBySize = chartService.buildIssueCountBySize(issuePeriods)
        val leadTimeCompareChart = chartService.calcLeadTimeCompareByPeriod(issuePeriods, board)

        return IssuePeriodChartResponse(
            issueCountBySize = issueCountBySize,
            leadTimeCompareChart = leadTimeCompareChart,
            leadTime = leadTime,
            issuesCount = issuesCount
        )
    }

}
