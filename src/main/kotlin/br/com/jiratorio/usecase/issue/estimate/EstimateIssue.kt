package br.com.jiratorio.usecase.issue.estimate

import br.com.jiratorio.client.IssueClient
import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.estimate.EstimatedIssue
import br.com.jiratorio.domain.jira.PagedIssueSearcher
import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.exception.MissingBoardConfigurationException
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.time.plusDays
import br.com.jiratorio.mapper.toEstimateIssueResponse
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.usecase.holiday.FindHolidayDays
import br.com.jiratorio.usecase.issue.FindIssueLeadTimes
import br.com.jiratorio.usecase.jql.CreateOpenedIssueJql
import br.com.jiratorio.usecase.parse.ParseEstimateIssue
import br.com.jiratorio.usecase.percentile.CalculatePercentile
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap

@UseCase
class EstimateIssue(
    private val boardRepository: BoardRepository,
    private val findIssueLeadTimes: FindIssueLeadTimes,
    private val issueClient: IssueClient,
    private val parseEstimateIssue: ParseEstimateIssue,
    private val createOpenedIssueJql: CreateOpenedIssueJql,
    private val jiraProperties: JiraProperties,
    private val findHolidayDays: FindHolidayDays,
    private val calculatePercentile: CalculatePercentile
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(
        boardId: Long,
        searchEstimateRequest: SearchEstimateRequest
    ): List<EstimateIssueResponse> {
        log.info("Action=estimateIssue, boardId={}, searchEstimateRequest={}", boardId, searchEstimateRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        if (board.fluxColumn.isNullOrEmpty()) {
            throw MissingBoardConfigurationException("fluxColumn")
        }

        val holidays = findHolidayDays.execute(board.id)

        val searcher = PagedIssueSearcher(
            issueClient = issueClient,
            issueParser = parseEstimateIssue
        )

        val jql = createOpenedIssueJql.execute(board)
        val estimateIssues = searcher.search(jql, board, holidays)

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
        val leadTimeList = findIssueLeadTimes.execute(board, issueForm)

        return calculatePercentile.execute(leadTimeList)
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

    private fun retrieveByFilter(issue: EstimatedIssue, filter: EstimateFieldReference): String? {
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
