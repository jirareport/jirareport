package br.com.jiratorio.service

import br.com.jiratorio.domain.EstimateFieldReference
import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.JiraIssue
import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.exception.MissingBoardConfigurationException
import br.com.jiratorio.extension.time.displayFormat
import br.com.jiratorio.extension.time.plusDays
import br.com.jiratorio.mapper.toChangelogResponse
import br.com.jiratorio.mapper.toImpedimentHistoryResponse
import br.com.jiratorio.property.JiraProperties
import br.com.jiratorio.provider.IssueProvider
import br.com.jiratorio.service.board.BoardService
import br.com.jiratorio.service.issue.IssueService
import br.com.jiratorio.strategy.impediment.ImpedimentCalculator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap

@Service
class EstimateIssueService(
    private val jiraProperties: JiraProperties,
    private val issueProvider: IssueProvider,
    private val percentileService: PercentileService,
    private val holidayService: HolidayService,
    private val boardService: BoardService,
    private val issueService: IssueService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findAll(boardId: Long, searchEstimateRequest: SearchEstimateRequest): List<EstimateIssueResponse> {
        log.info("Action=estimateIssue, boardId={}, searchEstimateRequest={}", boardId, searchEstimateRequest)

        val board = boardService.findById(boardId)

        if (board.fluxColumn.isNullOrEmpty()) {
            throw MissingBoardConfigurationException("fluxColumn")
        }

        val holidays = holidayService.findAllDaysByBoard(board.id)

        val (_, issues) = issueProvider.findOpenIssues(board, holidays)

        val fieldPercentileMap = HashMap<String, Percentile>()

        return issues.mapNotNull { jiraIssue ->
            retrieveByFilter(jiraIssue, searchEstimateRequest.filter)
                ?.let { value ->
                    val percentile: Percentile = fieldPercentileMap.getOrPut(value) {
                        calculatePercentile(board, searchEstimateRequest, value)
                    }

                    val estimateDateAvg = jiraIssue.startDate
                        .plusDays(percentile.average.toLong(), holidays, board.ignoreWeekend)

                    val estimateDatePercentile50 = jiraIssue.startDate
                        .plusDays(percentile.median, holidays, board.ignoreWeekend)

                    val estimateDatePercentile75 = jiraIssue.startDate
                        .plusDays(percentile.percentile75, holidays, board.ignoreWeekend)

                    val estimateDatePercentile90 = jiraIssue.startDate
                        .plusDays(percentile.percentile90, holidays, board.ignoreWeekend)

                    val (impedimentTime, impedimentHistory) = ImpedimentCalculator.from(board.impedimentType)
                        .calcImpediment(board.impedimentColumns, jiraIssue.changelog, jiraIssue.endDate, holidays, board.ignoreWeekend)

                    EstimateIssueResponse(
                        key = jiraIssue.key,
                        summary = jiraIssue.summary,
                        startDate = jiraIssue.startDate.displayFormat(),
                        estimateDateAvg = estimateDateAvg,
                        estimateDatePercentile50 = estimateDatePercentile50,
                        estimateDatePercentile75 = estimateDatePercentile75,
                        estimateDatePercentile90 = estimateDatePercentile90,
                        leadTime = jiraIssue.leadTime,
                        issueType = jiraIssue.issueType,
                        creator = jiraIssue.creator,
                        estimate = jiraIssue.estimate,
                        system = jiraIssue.system,
                        project = jiraIssue.project,
                        epic = jiraIssue.epic,
                        priority = jiraIssue.priority,
                        changelog = jiraIssue.changelog.columnChangelog.toChangelogResponse(),
                        impedimentTime = impedimentTime,
                        impedimentHistory = impedimentHistory.toImpedimentHistoryResponse(),
                        detailsUrl = "${jiraProperties.url}/browse/${jiraIssue.key}"
                    )
                }
        }
    }

    private fun calculatePercentile(board: BoardEntity, searchEstimateRequest: SearchEstimateRequest, value: String): Percentile {
        log.info("Method=calculatePercentile, board={}, searchEstimateRequest={}, value={}", board, searchEstimateRequest, value)
        val issueForm = buildIssueFormByEstimateForm(searchEstimateRequest, value)
        val leadTimeList = issueService.findLeadTimes(board, issueForm)

        return percentileService.calculate(leadTimeList)
    }

    private fun buildIssueFormByEstimateForm(searchEstimateRequest: SearchEstimateRequest, value: String): SearchIssueRequest {
        log.info("Method=buildIssueFormByEstimateForm, searchEstimateRequest={}, value={}", searchEstimateRequest, value)

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

    private fun retrieveByFilter(issue: JiraIssue, filter: EstimateFieldReference): String? {
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
