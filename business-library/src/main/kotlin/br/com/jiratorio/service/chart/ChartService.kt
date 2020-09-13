package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.domain.entity.embedded.IssueProgression
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.extension.time.daysBetween
import br.com.jiratorio.extension.time.displayFormat
import br.com.jiratorio.extension.time.isBetween
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toChart
import br.com.jiratorio.service.LeadTimeService
import br.com.jiratorio.service.PercentileService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class ChartService(
    private val messageResolver: MessageResolver,
    private val percentileService: PercentileService,
    private val leadTimeService: LeadTimeService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createCharts(issues: List<Issue>, board: BoardEntity): ChartAggregator {
        log.info("Action=createChartAggregator, issues={}, board={}", issues, board)

        return ChartAggregator(
            histogram = createHistogramChart(issues),
            leadTimeByEstimate = createEstimateLeadTimeChart(issues),
            throughputByEstimate = createEstimateThroughputChart(issues),
            leadTimeBySystem = createSystemLeadTimeChart(issues),
            throughputBySystem = createSystemThroughputChart(issues),
            leadTimeByType = createIssueTypeLeadTimeChart(issues),
            throughputByType = createIssueTypeThroughputChart(issues),
            leadTimeByProject = createProjectLeadTimeChart(issues),
            throughputByProject = createProjectThroughputChart(issues),
            leadTimeByPriority = createPriorityLeadTimeChart(issues),
            throughputByPriority = createPriorityThroughputChart(issues),
            leadTimeCompareChart = createLeadTimeCompareChart(issues),
            dynamicCharts = createDynamicChart(board, issues),
            issueProgression = createIssueProgressionChart(board, issues)
        )
    }

    private fun createHistogramChart(issues: List<Issue>): Histogram {
        log.info("Action=createHistogramChart, issues={}", issues)

        val leadTimeList = issues.map { it.leadTime }
        val percentile = percentileService.calculate(leadTimeList)
        val chart = histogramChart(issues)

        return Histogram(
            chart = chart,
            median = percentile.median,
            percentile75 = percentile.percentile75,
            percentile90 = percentile.percentile90
        )
    }

    private fun histogramChart(issues: List<Issue>): Chart<Long, Int> {
        log.info("Method=histogramChart, issues={}", issues)

        val collect: MutableMap<Long, Int> = issues
            .groupingBy { it.leadTime }
            .eachCount()
            .toMutableMap()

        val max = collect.keys.maxOrNull() ?: 1L
        for (i in 1 until max) {
            collect.putIfAbsent(i, 0)
        }

        return Chart(collect)
    }

    private fun createEstimateLeadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createEstimateLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues.groupBy { it.estimate ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun createEstimateThroughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createEstimateThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues.groupingBy { it.estimate ?: uninformedValue }
            .eachCount()
            .toChart()
    }

    private fun createSystemLeadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createSystemLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.system ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun createSystemThroughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createSystemThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupingBy { it.system ?: uninformedValue }
            .eachCount()
            .toChart()
    }

    private fun createIssueTypeLeadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createIssueTypeLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.issueType ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun createIssueTypeThroughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createIssueTypeThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")

        return issues
            .groupingBy { it.issueType ?: uninformedValue }
            .eachCount()
            .toChart()
    }

    private fun createProjectLeadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createPriorityThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.project ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun createProjectThroughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createProjectThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupingBy { it.project ?: uninformedValue }
            .eachCount()
            .toChart()
    }

    private fun createPriorityLeadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createPriorityLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.priority ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun createPriorityThroughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createPriorityThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupingBy { it.priority ?: uninformedValue }
            .eachCount()
            .toChart()
    }

    private fun createLeadTimeCompareChart(issues: List<Issue>): Chart<String, Double> {
        if (issues.isEmpty()) {
            return Chart()
        }

        if (issues.first() is IssueEntity) {
            @Suppress("UNCHECKED_CAST")
            return buildLeadTimeCompareChartWithIssueEntity(issues as List<IssueEntity>)
        }

        return leadTimeService.findAverageLeadTime(issues.map(Issue::id))
            .associate { it.name to it.value }
            .toChart()
    }

    private fun buildLeadTimeCompareChartWithIssueEntity(issues: List<IssueEntity>): Chart<String, Double> =
        issues
            .mapNotNull { it.leadTimes }
            .flatten()
            .groupBy { it.leadTimeConfig.name }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()

    private fun createDynamicChart(board: BoardEntity, issues: List<Issue>): List<DynamicChart> {
        log.info("Action=createDynamicChart, board={}, issues={}", board, issues)

        val dynamicFields = board.dynamicFields
        return if (dynamicFields.isNullOrEmpty()) {
            emptyList()
        } else {
            val uninformedValue = messageResolver.resolve("uninformed")

            dynamicFields.map {
                DynamicChart(
                    name = it.name,
                    leadTime = buildDynamicLeadTime(it, issues, uninformedValue),
                    throughput = buildDynamicThroughput(it, issues, uninformedValue)
                )
            }
        }
    }

    private fun buildDynamicLeadTime(
        config: DynamicFieldConfigEntity,
        issues: List<Issue>,
        uninformed: String,
    ): Chart<String, Double> {
        log.info("Method=buildDynamicLeadTime, config={}, issues={}", config, issues)

        return issues
            .groupBy { it.dynamicFields[config.name] ?: uninformed }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun buildDynamicThroughput(config: DynamicFieldConfigEntity, issues: List<Issue>, uninformed: String): Chart<String, Int> = issues
        .groupingBy { it.dynamicFields[config.name] ?: uninformed }
        .eachCount()
        .toChart()

    private fun createIssueProgressionChart(board: BoardEntity, issues: List<Issue>): IssueProgression {
        if (issues.isEmpty()) {
            return IssueProgression()
        }

        val sortedIssues = issues.sortedBy(Issue::startDate)

        val start = sortedIssues.first()
            .startDate
            .toLocalDate()

        val end = issues
            .maxByOrNull(Issue::endDate)
            ?.endDate
            ?.toLocalDate()

        val days = start.daysBetween(
            endDate = end,
            holidays = board.holidays?.map(HolidayEntity::date) ?: emptyList(),
            ignoreWeekend = board.ignoreWeekend
        )

        val result = sortedIssues.asSequence()
            .map { Triple(it.key, it.startDate.toLocalDate(), it.endDate.toLocalDate()) }
            .map { (key, startDate, endDate) ->
                val daysInProgress = days.map { day ->
                    day.isBetween(startDate, endDate)
                }

                Pair(key, daysInProgress)
            }
            .toMap()

        return IssueProgression(
            days = days.map { it.displayFormat(DateTimeFormatter.ofPattern("dd/MM")) },
            issues = result
        )
    }

}
