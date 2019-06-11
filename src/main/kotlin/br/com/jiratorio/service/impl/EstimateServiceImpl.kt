package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.config.properties.JiraProperties
import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.exception.BadRequestException
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.plusDays
import br.com.jiratorio.mapper.toEstimateIssueResponse
import br.com.jiratorio.parser.EstimateIssueParser
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.EstimateService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.PercentileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap

@Service
class EstimateServiceImpl(
    private val boardService: BoardService,
    private val issueService: IssueService,
    private val issueClient: IssueClient,
    private val estimateIssueParser: EstimateIssueParser,
    private val holidayService: HolidayService,
    private val percentileService: PercentileService,
    private val jqlService: JQLService,
    private val messageResolver: MessageResolver,
    private val jiraProperties: JiraProperties
) : EstimateService {

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findEstimateIssues(
        boardId: Long,
        searchEstimateRequest: SearchEstimateRequest
    ): List<EstimateIssueResponse> {
        log.info("Method=findEstimateIssues, boardId={}, searchEstimateRequest={}", boardId, searchEstimateRequest)

        val board = boardService.findById(boardId)

        if (board.fluxColumn.isNullOrEmpty()) {
            throw BadRequestException("board.fluxColumn", messageResolver.resolve("errors.flux-column-not-configured"))
        }

        val estimateIssues = estimateIssueParser.parseEstimate(
            root = issueClient.findByJql(
                jql = jqlService.openedIssues(board)
            ),
            board = board
        )

        val holidays = holidayService.findDaysByBoard(boardId)

        val fieldPercentileMap = HashMap<String, Percentile>()
        for (estimateIssue in estimateIssues) {
            val value = retrieveByFilter(estimateIssue, searchEstimateRequest.filter) ?: continue

            val percentile: Percentile = fieldPercentileMap.getOrPut(value) {
                calculatePercentile(board, searchEstimateRequest, value)
            }

            estimateIssue.estimateDateAvg = estimateIssue.startDate.plusDays(
                percentile.average.toLong(), holidays, board.ignoreWeekend
            )

            estimateIssue.estimateDatePercentile50 = estimateIssue.startDate.plusDays(
                percentile.median, holidays, board.ignoreWeekend
            )

            estimateIssue.estimateDatePercentile75 = estimateIssue.startDate.plusDays(
                percentile.percentile75, holidays, board.ignoreWeekend
            )

            estimateIssue.estimateDatePercentile90 = estimateIssue.startDate.plusDays(
                percentile.percentile90, holidays, board.ignoreWeekend
            )
        }

        return estimateIssues.toEstimateIssueResponse(jiraProperties.url)
    }

    private fun calculatePercentile(
        board: Board,
        searchEstimateRequest: SearchEstimateRequest,
        value: String
    ): Percentile {
        log.info(
            "Method=calculatePercentile, board={}, searchEstimateRequest={}, value={}",
            board, searchEstimateRequest, value
        )
        val issueForm = buildIssueFormByEstimateForm(searchEstimateRequest, value)
        val leadTimeList = issueService.findLeadTimeByExample(board, issueForm)

        return percentileService.calculatePercentile(leadTimeList)
    }

    private fun buildIssueFormByEstimateForm(
        searchEstimateRequest: SearchEstimateRequest,
        value: String
    ): SearchIssueRequest {
        log.info(
            "Method=buildIssueFormByEstimateForm, searchEstimateRequest={}, value={}",
            searchEstimateRequest, value
        )

        val searchIssueRequest = SearchIssueRequest(
            startDate = searchEstimateRequest.startDate,
            endDate = searchEstimateRequest.endDate
        )

        when (searchEstimateRequest.filter) {
            EstimateFieldReference.ISSUE_TYPE -> searchIssueRequest.issueTypes.add(value)
            EstimateFieldReference.SYSTEM -> searchIssueRequest.systems.add(value)
            EstimateFieldReference.ESTIMATE -> searchIssueRequest.estimates.add(value)
            EstimateFieldReference.EPIC -> searchIssueRequest.epics.add(value)
            EstimateFieldReference.PROJECT -> searchIssueRequest.projects.add(value)
            EstimateFieldReference.PRIORITY -> searchIssueRequest.priorities.add(value)
        }

        return searchIssueRequest
    }

    private fun retrieveByFilter(issue: EstimateIssue, filter: EstimateFieldReference): String? {
        log.info("Method=retrieveByFilter, issue={}, filter={}", issue, filter)
        return when (filter) {
            EstimateFieldReference.ISSUE_TYPE -> issue.issueType
            EstimateFieldReference.SYSTEM -> issue.system
            EstimateFieldReference.ESTIMATE -> issue.estimate
            EstimateFieldReference.EPIC -> issue.epic
            EstimateFieldReference.PROJECT -> issue.project
            EstimateFieldReference.PRIORITY -> issue.priority
        }
    }

}
