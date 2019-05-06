package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.IssueDetailResponse
import br.com.jiratorio.domain.response.IssueFilterResponse
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.mapper.IssueMapper
import br.com.jiratorio.parser.IssueParser
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.WeeklyThroughputService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.leadtime.LeadTimeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class IssueServiceImpl(
    private val issueClient: IssueClient,
    private val issueParser: IssueParser,
    private val issueRepository: IssueRepository,
    private val issueMapper: IssueMapper,
    private val chartService: ChartService,
    private val leadTimeService: LeadTimeService,
    private val boardService: BoardService,
    private val weeklyThroughputService: WeeklyThroughputService
) : IssueService {

    @ExecutionTime
    @Transactional
    override fun createByJql(jql: String, board: Board): List<Issue> {
        log.info("Method=createByJql, jql={}, board={}", jql, board)

        val issuesStr = issueClient.findByJql(jql)
        val issues = issueParser.parse(issuesStr, board)

        issueRepository.saveAll(issues)
        leadTimeService.createLeadTimes(issues, board.id)

        return issues
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findByExample(boardId: Long, searchIssueRequest: SearchIssueRequest): ListIssueResponse {
        log.info("Method=findByExample, boardId={}, searchIssueRequest={}", boardId, searchIssueRequest)

        val issues = issueRepository.findByExample(boardId, searchIssueRequest)

        val board = boardService.findById(boardId)
        val chartAggregator = chartService.buildAllCharts(issues, board)

        val leadTime = issues.map { it.leadTime }.average()

        val weeklyThroughput = weeklyThroughputService.calcWeeklyThroughput(
            searchIssueRequest.startDate,
            searchIssueRequest.endDate,
            issues
        )

        return ListIssueResponse(
            issues = issueMapper.issueToIssueResponse(issues),
            charts = chartAggregator,
            leadTime = leadTime,
            weeklyThroughput = weeklyThroughput
        )
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findLeadTimeByExample(boardId: Long, searchIssueRequest: SearchIssueRequest): List<Long> {
        log.info("Method=findLeadTimeByExample, board={}, searchIssueRequest={}", boardId, searchIssueRequest)
        return issueRepository.findByExample(boardId, searchIssueRequest)
            .map { it.leadTime }
    }

    @Transactional
    override fun deleteAll(issues: List<Issue>) {
        log.info("Method=deleteAll, issues={}", issues)
        issueRepository.deleteAll(issues)
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findFilters(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueFilterResponse {
        log.info("Method=findFilters, boardId={}, startDate={}, endDate={}", boardId, startDate, endDate)

        return IssueFilterResponse(
            estimates = issueRepository.findAllEstimatesByBoardId(boardId),
            keys = issueRepository.findAllKeysByBoardIdAndDates(
                boardId,
                startDate.atStartOfDay(),
                endDate.atEndOfDay()
            ),
            systems = issueRepository.findAllSystemsByBoardId(boardId),
            epics = issueRepository.findAllEpicsByBoardId(boardId),
            issueTypes = issueRepository.findAllIssueTypesByBoardId(boardId),
            projects = issueRepository.findAllIssueProjectsByBoardId(boardId),
            priorities = issueRepository.findAllIssuePrioritiesByBoardId(boardId),
            dynamicFieldsValues = issueRepository.findAllDynamicFieldValues(boardId)
        )
    }

    @Transactional(readOnly = true)
    override fun findByBoardAndId(boardId: Long, id: Long): IssueDetailResponse {
        log.info("Method=findByBoardAndId, boardId={}, id={}", boardId, id)

        val issue = issueRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return issueMapper.issueToIssueDetailResponse(issue)
    }
}
