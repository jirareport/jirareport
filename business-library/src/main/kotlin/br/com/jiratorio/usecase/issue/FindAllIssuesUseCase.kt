package br.com.jiratorio.usecase.issue

import br.com.jiratorio.domain.issue.MinimalIssue
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ColumnTimeAverageResponse
import br.com.jiratorio.domain.response.IssueListResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.property.JiraProperties
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.ColumnTimeAverageRepository
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.usecase.chart.CreateChartAggregatorUseCase
import br.com.jiratorio.usecase.weeklythroughput.CalculateWeeklyThroughputUseCase
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllIssuesUseCase(
    private val boardRepository: BoardRepository,
    private val issueRepository: IssueRepository,
    private val createChartAggregator: CreateChartAggregatorUseCase,
    private val jiraProperties: JiraProperties,
    private val calculateWeeklyThroughput: CalculateWeeklyThroughputUseCase,
    private val columnTimeAverageRepository: ColumnTimeAverageRepository
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

        val columnTimeAverages = retrieveColumnTimeAverages(board, issues)

        return IssueListResponse(
            leadTime = leadTime.zeroIfNaN(),
            throughput = issues.size,
            issues = issues.toIssueResponse(jiraProperties.url),
            charts = chartAggregator,
            columnTimeAverages = columnTimeAverages,
            weeklyThroughput = weeklyThroughput
        )
    }

    private fun retrieveColumnTimeAverages(
        board: BoardEntity,
        issues: List<Issue>
    ): List<ColumnTimeAverageResponse> {
        val fluxColumn = board.fluxColumn ?: emptyList()

        return columnTimeAverageRepository.findColumnTimeAverage(issues.map { it.id })
            .sortedBy { (to, _) -> fluxColumn.indexOf(to.toUpperCase()) }
    }

}
