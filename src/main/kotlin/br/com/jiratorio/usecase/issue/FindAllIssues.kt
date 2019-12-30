package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.IssueListResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.usecase.chart.CreateChartAggregator
import br.com.jiratorio.usecase.weeklythroughput.CalculateWeeklyThroughput
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllIssues(
    private val boardRepository: BoardRepository,
    private val issueRepository: IssueRepository,
    private val createChartAggregator: CreateChartAggregator,
    private val jiraProperties: JiraProperties,
    private val calculateWeeklyThroughput: CalculateWeeklyThroughput
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(
        boardId: Long,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): IssueListResponse {
        log.info(
            "Action=findAllIssues, boardId={}, dynamicFilters={}, searchIssueRequest={}",
            boardId, dynamicFilters, searchIssueRequest
        )

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        val issues = issueRepository.findByExample(board, dynamicFilters, searchIssueRequest)
        val chartAggregator = createChartAggregator.execute(issues, board)

        val leadTime = issues.map { it.leadTime }.average()

        val weeklyThroughput = calculateWeeklyThroughput.execute(
            startDate = searchIssueRequest.startDate,
            endDate = searchIssueRequest.endDate,
            issues = issues
        )

        return IssueListResponse(
            leadTime = leadTime.zeroIfNaN(),
            throughput = issues.size,
            issues = issues.toIssueResponse(jiraProperties.url),
            charts = chartAggregator,
            weeklyThroughput = weeklyThroughput
        )
    }
}
