package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.usecase.chart.CreateChartAggregator
import br.com.jiratorio.usecase.columntimeaverage.CreateColumnTimeAverages
import br.com.jiratorio.usecase.issue.create.CreateIssues
import br.com.jiratorio.usecase.wip.CalculateAverageWip
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateIssuePeriod(
    private val issuePeriodRepository: IssuePeriodRepository,
    private val boardRepository: BoardRepository,
    private val createChartAggregator: CreateChartAggregator,
    private val calculateAverageWip: CalculateAverageWip,
    private val createColumnTimeAverages: CreateColumnTimeAverages,
    private val createIssues: CreateIssues,
    private val messageResolver: MessageResolver,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        log.info("Action=createIssuePeriod, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId)

        val (startDate, endDate) = createIssuePeriodRequest

        issuePeriodRepository.deleteByStartDateAndEndDateAndBoardId(startDate, endDate, boardId)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        val issuePeriod = IssuePeriod(
            name = board.issuePeriodNameFormat.format(startDate, endDate, messageResolver.locale),
            startDate = startDate,
            endDate = endDate,
            board = board
        )

        issuePeriodRepository.save(issuePeriod)

        val (jql, issues) = createIssues.execute(board, issuePeriod.id, startDate, endDate)

        issuePeriod.jql = jql
        issuePeriod.issues = issues.toMutableSet()
        issuePeriod.leadTime = issues.map(Issue::leadTime).average().zeroIfNaN()
        issuePeriod.throughput = issues.size
        issuePeriod.avgPctEfficiency = issues.map(Issue::pctEfficiency).average().zeroIfNaN()
        issuePeriod.wipAvg = calculateAverageWip.execute(
            startDate,
            endDate,
            issues,
            FluxColumn(board).wipColumns
        )

        createColumnTimeAverages.execute(issuePeriod, board.fluxColumn ?: emptyList())

        createCharts(issuePeriod, board)

        issuePeriodRepository.save(issuePeriod)

        return issuePeriod.id
    }

    private fun createCharts(
        issuePeriod: IssuePeriod,
        board: Board,
    ) {
        val chartAggregator = createChartAggregator.execute(
            issuePeriod.issues.map {
                MinimalIssue(
                    id = it.id,
                    key = it.key,
                    leadTime = it.leadTime,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    creator = it.creator,
                    summary = it.summary,
                    issueType = it.issueType,
                    estimate = it.estimate,
                    project = it.project,
                    epic = it.epic,
                    system = it.system,
                    priority = it.priority,
                    created = it.created,
                    deviationOfEstimate = it.deviationOfEstimate,
                    changeEstimateCount = it.dueDateHistory?.size ?: 0,
                    impedimentTime = it.impedimentTime,
                    dynamicFields = it.dynamicFields ?: emptyMap()
                )
            },
            board
        )

        issuePeriod.apply {
            histogram = chartAggregator.histogram
            leadTimeByEstimate = chartAggregator.leadTimeByEstimate
            throughputByEstimate = chartAggregator.throughputByEstimate
            leadTimeBySystem = chartAggregator.leadTimeBySystem
            throughputBySystem = chartAggregator.throughputBySystem
            leadTimeByType = chartAggregator.leadTimeByType
            throughputByType = chartAggregator.throughputByType
            leadTimeByProject = chartAggregator.leadTimeByProject
            throughputByProject = chartAggregator.throughputByProject
            leadTimeCompareChart = chartAggregator.leadTimeCompareChart
            leadTimeByPriority = chartAggregator.leadTimeByPriority
            throughputByPriority = chartAggregator.throughputByPriority
            dynamicCharts = chartAggregator.dynamicCharts.toMutableList()
            issueProgression = chartAggregator.issueProgression
        }
    }

}
