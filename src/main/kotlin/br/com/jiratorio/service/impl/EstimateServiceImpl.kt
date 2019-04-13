package br.com.jiratorio.service.impl

import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.form.EstimateForm
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.exception.BadRequestException
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.plusDays
import br.com.jiratorio.parser.EstimateIssueParser
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.EstimateService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.PercentileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
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
    private val messageResolver: MessageResolver
) : EstimateService {

    @Transactional(readOnly = true)
    override fun findEstimateIssues(boardId: Long, estimateForm: EstimateForm): List<EstimateIssue> {
        log.info("Method=findEstimateIssues, boardId={}, estimateForm={}", boardId, estimateForm)

        val board = boardService.findById(boardId)

        if (CollectionUtils.isEmpty(board.fluxColumn)) {
            throw BadRequestException(messageResolver.resolve("errors.flux-column-not-configured"))
        }

        val jql = jqlService.openedIssues(board)
        val issueList = findEstimateByJql(jql, board)

        val holidays = if (board.ignoreWeekend == true)
            emptyList()
        else
            holidayService.findDaysByBoard(boardId)

        val fieldPercentileMap = HashMap<String, Percentile>()

        for (issue in issueList) {
            val value = retrieveByFilter(issue, estimateForm.filter) ?: continue

            val percentile: Percentile = fieldPercentileMap.getOrPut(value) {
                calculatePercentile(board, estimateForm, value)
            }

            issue.estimateDateAvg = issue.startDate.plusDays(
                percentile.average.toLong(), holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile50 = issue.startDate.plusDays(
                percentile.median, holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile75 = issue.startDate.plusDays(
                percentile.percentile75, holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile90 = issue.startDate.plusDays(
                percentile.percentile90, holidays, board.ignoreWeekend
            )
        }

        return issueList
    }

    private fun findEstimateByJql(jql: String, board: Board): List<EstimateIssue> {
        val issues = issueClient.findByJql(jql)
        return estimateIssueParser.parseEstimate(issues, board)
    }

    private fun calculatePercentile(board: Board, estimateForm: EstimateForm, value: String): Percentile {
        log.info("Method=calculatePercentile, board={}, estimateForm={}, value={}", board, estimateForm, value)

        val issueForm = buildIssueFormByEstimateForm(estimateForm, value)

        val leadTimeList = issueService.findLeadTimeByExample(board.id, issueForm)

        return percentileService.calculatePercentile(leadTimeList)
    }

    private fun buildIssueFormByEstimateForm(estimateForm: EstimateForm, value: String): SearchIssueRequest {
        log.info("Method=buildIssueFormByEstimateForm, estimateForm={}, value={}", estimateForm, value)

        val searchIssueRequest = SearchIssueRequest(
            startDate = estimateForm.startDate,
            endDate = estimateForm.endDate
        )

        when (estimateForm.filter) {
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
            EstimateFieldReference.ESTIMATE -> issue.estimated
            EstimateFieldReference.EPIC -> issue.epic
            EstimateFieldReference.PROJECT -> issue.project
            EstimateFieldReference.PRIORITY -> issue.priority
        }
    }

}
