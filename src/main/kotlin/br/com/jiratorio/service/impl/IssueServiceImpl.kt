package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueFilterKeyResponse
import br.com.jiratorio.domain.response.issue.IssueFilterResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.mapper.toIssueDetailResponse
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.WeeklyThroughputService
import br.com.jiratorio.service.chart.ChartService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class IssueServiceImpl(
    private val issueRepository: IssueRepository,
    private val chartService: ChartService,
    private val boardService: BoardService,
    private val weeklyThroughputService: WeeklyThroughputService,
    private val jiraProperties: JiraProperties
) : IssueService {

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findByExample(
        boardId: Long,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): ListIssueResponse {
        log.info(
            "Method=findByExample, boardId={}, dynamicFilters={}, searchIssueRequest={}",
            boardId, dynamicFilters, searchIssueRequest
        )

        val board = boardService.findById(boardId)

        val issues = issueRepository.findByExample(board, dynamicFilters, searchIssueRequest)
        val chartAggregator = chartService.buildAllCharts(issues, board)

        val leadTime = issues.map { it.leadTime }.average()

        val weeklyThroughput = weeklyThroughputService.calcWeeklyThroughput(
            searchIssueRequest.startDate,
            searchIssueRequest.endDate,
            issues
        )

        return ListIssueResponse(
            leadTime = leadTime.zeroIfNaN(),
            throughput = issues.size,
            issues = issues.toIssueResponse(jiraProperties.url),
            charts = chartAggregator,
            weeklyThroughput = weeklyThroughput
        )
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findLeadTimeByExample(board: Board, searchIssueRequest: SearchIssueRequest): List<Long> {
        log.info("Method=findLeadTimeByExample, board={}, searchIssueRequest={}", board, searchIssueRequest)
        return issueRepository.findByExample(board, emptyMap(), searchIssueRequest)
            .map { it.leadTime }
    }

    @Transactional
    override fun deleteAll(issues: Set<Issue>) {
        log.info("Method=deleteAll, issues={}", issues)
        issueRepository.deleteAll(issues)
    }

    @Transactional(readOnly = true)
    override fun findByBoardAndId(boardId: Long, id: Long): IssueDetailResponse {
        log.info("Method=findByBoardAndId, boardId={}, id={}", boardId, id)

        val issue = issueRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return issue.toIssueDetailResponse()
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findFilters(boardId: Long): IssueFilterResponse {
        log.info("Method=findFilters, boardId={}", boardId)

        return IssueFilterResponse(
            estimates = issueRepository.findAllEstimatesByBoardId(boardId),
            systems = issueRepository.findAllSystemsByBoardId(boardId),
            epics = issueRepository.findAllEpicsByBoardId(boardId),
            issueTypes = issueRepository.findAllIssueTypesByBoardId(boardId),
            projects = issueRepository.findAllIssueProjectsByBoardId(boardId),
            priorities = issueRepository.findAllIssuePrioritiesByBoardId(boardId),
            dynamicFieldsValues = issueRepository.findAllDynamicFieldValues(boardId)
        )
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findFilterKeys(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueFilterKeyResponse {
        return IssueFilterKeyResponse(
            keys = issueRepository.findAllKeysByBoardIdAndDates(
                boardId = boardId,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atEndOfDay()
            )
        )
    }

}
