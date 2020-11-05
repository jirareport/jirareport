package br.com.jiratorio.service.issue

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.IssueListResponse
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueFilterResponse
import br.com.jiratorio.domain.response.issue.IssueKeysResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.mapper.toIssueDetailResponse
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.property.JiraProperties
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.board.BoardService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.ColumnTimeAverageService
import br.com.jiratorio.service.WeeklyThroughputService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class IssueService(
    private val jiraProperties: JiraProperties,
    private val boardService: BoardService,
    private val weeklyThroughputService: WeeklyThroughputService,
    private val columnTimeAverageService: ColumnTimeAverageService,
    private val chartService: ChartService,
    private val issueRepository: IssueRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(boardId: Long, dynamicFilters: Map<String, Array<String>>, searchIssueRequest: SearchIssueRequest): IssueListResponse {
        val board = boardService.findById(boardId)

        val issues = issueRepository.findByExample(board, dynamicFilters, searchIssueRequest)
        val chartAggregator = chartService.createCharts(issues, board)

        val leadTime = issues.map { it.leadTime }.average()

        val weeklyThroughput = weeklyThroughputService.calculate(
            startDate = searchIssueRequest.startDate,
            endDate = searchIssueRequest.endDate,
            issues = issues
        )

        val columnTimeAverages = columnTimeAverageService.retrieveColumnTimeAverages(board, issues)

        return IssueListResponse(
            leadTime = leadTime.zeroIfNaN(),
            throughput = issues.size,
            issues = issues.toIssueResponse(jiraProperties.url),
            charts = chartAggregator,
            columnTimeAverages = columnTimeAverages,
            weeklyThroughput = weeklyThroughput
        )
    }

    @Transactional(readOnly = true)
    fun findByIdAndBoard(id: Long, boardId: Long): IssueDetailResponse {
        val issue = issueRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return issue.toIssueDetailResponse()
    }

    @Transactional(readOnly = true)
    fun findAllFilters(boardId: Long): IssueFilterResponse =
        IssueFilterResponse(
            estimates = issueRepository.findAllEstimatesByBoardId(boardId),
            systems = issueRepository.findAllSystemsByBoardId(boardId),
            epics = issueRepository.findAllEpicsByBoardId(boardId),
            issueTypes = issueRepository.findAllIssueTypesByBoardId(boardId),
            projects = issueRepository.findAllIssueProjectsByBoardId(boardId),
            priorities = issueRepository.findAllIssuePrioritiesByBoardId(boardId),
            dynamicFieldsValues = issueRepository.findAllDynamicFieldValues(boardId)
        )

    @Transactional(readOnly = true)
    fun findKeys(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueKeysResponse =
        IssueKeysResponse(
            keys = issueRepository.findAllKeysByBoardIdAndDates(
                boardId = boardId,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atEndOfDay()
            )
        )

    @Transactional(readOnly = true)
    fun findLeadTimes(board: BoardEntity, searchIssueRequest: SearchIssueRequest): List<Long> =
        issueRepository.findByExample(board, emptyMap(), searchIssueRequest)
            .map { it.leadTime }

}
