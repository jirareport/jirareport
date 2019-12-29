package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.WeeklyThroughputService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.usecase.board.FindBoard
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssueByExample(
    private val findBoard: FindBoard,
    private val issueRepository: IssueRepository,
    private val chartService: ChartService,
    private val weeklyThroughputService: WeeklyThroughputService,
    private val jiraProperties: JiraProperties
) {

    @Transactional(readOnly = true)
    fun execute(
        boardId: Long,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): ListIssueResponse {
        log.info(
            "Method=findByExample, boardId={}, dynamicFilters={}, searchIssueRequest={}",
            boardId, dynamicFilters, searchIssueRequest
        )

        val board = findBoard.execute(boardId)

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
}
