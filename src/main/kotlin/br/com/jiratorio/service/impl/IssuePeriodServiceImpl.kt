package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByBoardResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.format
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.mapper.IssueMapper
import br.com.jiratorio.mapper.IssuePeriodMapper
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.IssuePeriodService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.WipService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.chart.IssuePeriodChartService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class IssuePeriodServiceImpl(
    private val issueService: IssueService,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val chartService: ChartService,
    private val leadTimeChartService: IssuePeriodChartService,
    private val boardService: BoardService,
    private val wipService: WipService,
    private val jqlService: JQLService,
    private val issuePeriodMapper: IssuePeriodMapper,
    private val issueMapper: IssueMapper
) : IssuePeriodService {

    @Transactional
    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Method=create, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)

        val (startDate, endDate) = createIssuePeriodRequest

        delete(startDate, endDate, boardId)

        val board = boardService.findById(boardId)

        val jql = jqlService.finalizedIssues(board, startDate, endDate)
        val issues = issueService.createByJql(jql, board)

        val leadTime = issues.map { it.leadTime }.average().zeroIfNaN()
        val avgPctEfficiency = issues.map { it.pctEfficiency }.average().zeroIfNaN()

        val chartAggregator = chartService.buildAllCharts(issues, board)

        val fluxColumn = FluxColumn(board)
        val wipAvg = wipService.calcAvgWip(startDate, endDate, issues, fluxColumn.wipColumns)

        val issuePeriod = IssuePeriod(
            startDate = startDate,
            endDate = endDate,
            boardId = boardId,
            issues = issues.toMutableList(),
            leadTime = leadTime,
            histogram = chartAggregator.histogram,
            leadTimeByEstimate = chartAggregator.throughputByEstimate,
            throughputByEstimate = chartAggregator.leadTimeByEstimate,
            leadTimeBySystem = chartAggregator.leadTimeBySystem,
            throughputBySystem = chartAggregator.throughputBySystem,
            columnTimeAvg = chartAggregator.columnTimeAvg.toMutableList(),
            leadTimeByType = chartAggregator.leadTimeByType,
            throughputByType = chartAggregator.throughputByType,
            leadTimeByProject = chartAggregator.leadTimeByProject,
            throughputByProject = chartAggregator.throughputByProject,
            leadTimeCompareChart = chartAggregator.leadTimeCompareChart,
            leadTimeByPriority = chartAggregator.leadTimeByPriority,
            throughputByPriority = chartAggregator.throughputByPriority,
            dynamicCharts = chartAggregator.dynamicCharts.toMutableList(),
            throughput = issues.size,
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
    override fun removeByBoardAndId(boardId: Long, id: Long) {
        log.info("Method=removeByBoardAndId, boardId={}, id={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        delete(issuePeriod)
    }

    @Transactional(readOnly = true)
    override fun findById(boardId: Long, id: Long): IssuePeriodByIdResponse {
        log.info("Method=findById, id={}", id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return IssuePeriodByIdResponse(
            detail = issuePeriodMapper.issuePeriodToIssuePeriodDetailResponse(issuePeriod),
            issues = issueMapper.issueToIssueResponse(issuePeriod.issues)
        )
    }

    @Transactional
    override fun update(boardId: Long, id: Long) {
        log.info("Method=update, boardId={}, id={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        val createIssuePeriodRequest = CreateIssuePeriodRequest(issuePeriod.startDate, issuePeriod.endDate)

        create(createIssuePeriodRequest, issuePeriod.boardId)
    }

    @Transactional(readOnly = true)
    override fun findIssuePeriodByBoard(boardId: Long): IssuePeriodByBoardResponse {
        log.info("Method=findIssuePeriodByBoard, boardId={}", boardId)

        val board = boardService.findById(boardId)

        val issuePeriods = issuePeriodRepository.findByBoardId(boardId)
            .sortedBy { it.startDate }

        return IssuePeriodByBoardResponse(
            periods = issuePeriodMapper.issuePeriodToIssuePeriodResponse(issuePeriods),
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

        val throughputByEstimate = leadTimeChartService.throughputByEstimate(issuePeriods)
        val leadTimeCompareChart = leadTimeChartService.leadTimeCompareByPeriod(issuePeriods, board)

        return IssuePeriodChartResponse(
            throughputByEstimate = throughputByEstimate,
            leadTimeCompareChart = leadTimeCompareChart,
            leadTime = leadTime,
            throughput = throughput
        )
    }

}
