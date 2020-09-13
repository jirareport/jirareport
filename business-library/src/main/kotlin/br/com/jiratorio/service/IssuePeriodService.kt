package br.com.jiratorio.service

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodListResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.decimal.format
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toIssuePeriodDetailResponse
import br.com.jiratorio.mapper.toIssuePeriodResponse
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.property.JiraProperties
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.service.board.BoardService
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.service.chart.PeriodChartService
import br.com.jiratorio.service.issue.CreateIssueService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssuePeriodService(
    private val jiraProperties: JiraProperties,
    private val messageResolver: MessageResolver,
    private val boardService: BoardService,
    private val columnTimeAverageService: ColumnTimeAverageService,
    private val wipService: WipService,
    private val createIssueService: CreateIssueService,
    private val chartService: ChartService,
    private val periodChartService: PeriodChartService,
    private val issuePeriodRepository: IssuePeriodRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(filter: FindAllIssuePeriodsFilter): IssuePeriodListResponse {
        val boardPreferences = boardService.findIssuePeriodPreferencesByBoard(filter.boardId)

        val issuePeriods = issuePeriodRepository.findAll(filter, boardPreferences)

        return IssuePeriodListResponse(
            periods = issuePeriods.toIssuePeriodResponse(jiraProperties.url),
            charts = buildCharts(issuePeriods, filter, boardPreferences)
        )
    }

    private fun buildCharts(issuePeriods: List<MinimalIssuePeriod>, filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): IssuePeriodChartResponse {
        val leadTime: Chart<String, String> = Chart()
        val throughput: Chart<String, Int> = Chart()

        for (issuePeriod in issuePeriods) {
            leadTime[issuePeriod.name] = issuePeriod.leadTime.format()
            throughput[issuePeriod.name] = issuePeriod.throughput
        }

        return IssuePeriodChartResponse(
            leadTime = leadTime,
            throughput = throughput,
            leadTimeCompareChart = periodChartService.leadTimeCompare(filter, boardPreferences),
            throughputByEstimate = periodChartService.throughputByEstimate(filter, boardPreferences),
            issueTypePerformanceCompareChart = periodChartService.issueTypePerformanceCompare(filter, boardPreferences)
        )
    }

    @Transactional(readOnly = true)
    fun findByIdAndBoard(id: Long, boardId: Long): IssuePeriodByIdResponse {
        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        return IssuePeriodByIdResponse(
            detail = issuePeriod.toIssuePeriodDetailResponse(),
            issues = issuePeriod.issues.map { it.toIssueResponse(jiraProperties.url) }
        )
    }

    @Transactional
    fun update(id: Long, boardId: Long) {
        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        val createIssuePeriodRequest = CreateIssuePeriodRequest(issuePeriod.startDate, issuePeriod.endDate)

        create(createIssuePeriodRequest, boardId)
    }

    @Transactional
    fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long {
        val (startDate, endDate) = createIssuePeriodRequest

        issuePeriodRepository.deleteByStartDateAndEndDateAndBoardId(startDate, endDate, boardId)

        val board = boardService.findById(boardId)

        val issuePeriod = IssuePeriodEntity(
            name = board.issuePeriodNameFormat.format(startDate, endDate, messageResolver.locale),
            startDate = startDate,
            endDate = endDate,
            board = board
        )

        issuePeriodRepository.save(issuePeriod)

        val (jql, issues) = createIssueService.create(board, issuePeriod.id, startDate, endDate)

        issuePeriod.jql = jql
        issuePeriod.issues = issues.toMutableSet()
        issuePeriod.leadTime = issues.map(IssueEntity::leadTime).average().zeroIfNaN()
        issuePeriod.throughput = issues.size
        issuePeriod.avgPctEfficiency = issues.map(IssueEntity::pctEfficiency).average().zeroIfNaN()
        issuePeriod.wipAvg = wipService.calculateAverage(
            startDate,
            endDate,
            issues,
            FluxColumn(board).wipColumns
        )

        columnTimeAverageService.create(issuePeriod, board.fluxColumn ?: emptyList())

        createCharts(issuePeriod, board)

        issuePeriodRepository.save(issuePeriod)

        return issuePeriod.id
    }

    private fun createCharts(issuePeriod: IssuePeriodEntity, board: BoardEntity) {
        val chartAggregator = chartService.createCharts(issuePeriod.issues.toList(), board)

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

    @Transactional
    fun delete(id: Long, boardId: Long) {
        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        issuePeriodRepository.delete(issuePeriod)
    }
}
