package br.com.jiratorio.service.impl

import br.com.jiratorio.config.property.JiraProperties
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
import br.com.jiratorio.mapper.toIssuePeriodDetailResponse
import br.com.jiratorio.mapper.toIssuePeriodResponse
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.CreateIssueService
import br.com.jiratorio.service.IssuePeriodService
import br.com.jiratorio.service.chart.IssuePeriodChartService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssuePeriodServiceImpl(
    private val issuePeriodRepository: IssuePeriodRepository,
    private val leadTimeChartService: IssuePeriodChartService,
    private val boardService: BoardService,
    private val createIssueService: CreateIssueService,
    private val jiraProperties: JiraProperties
) : IssuePeriodService {

    @Transactional
    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Method=create, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)
        return createIssueService.create(createIssuePeriodRequest, boardId)
    }

    @Transactional
    override fun removeByBoardAndId(boardId: Long, id: Long) {
        log.info("Method=removeByBoardAndId, boardId={}, id={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        issuePeriodRepository.delete(issuePeriod)
    }

    @Transactional(readOnly = true)
    override fun findById(boardId: Long, id: Long): IssuePeriodByIdResponse {
        log.info("Method=findByBoardAndId, id={}", id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return IssuePeriodByIdResponse(
            detail = issuePeriod.toIssuePeriodDetailResponse(),
            issues = issuePeriod.issues.toIssueResponse(jiraProperties.url)
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
