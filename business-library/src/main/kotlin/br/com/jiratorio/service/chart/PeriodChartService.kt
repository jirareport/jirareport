package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.ChartRepository
import org.springframework.stereotype.Service

@Service
class PeriodChartService(
    private val chartRepository: ChartRepository,
    private val messageResolver: MessageResolver,
) {

    fun issueTypePerformanceCompare(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): Map<String, MultiAxisChart<Number>> {
        val uninformedValue = messageResolver.resolve("uninformed")
        return chartRepository.findPerformanceComparisonByIssueType(filter)
            .groupBy { it.issueType ?: uninformedValue }
            .mapValues {
                it.value
                    .associate { performance ->
                        Pair(
                            boardPreferences.issuePeriodNameFormat.format(performance.periodStart, performance.periodEnd),
                            mapOf(
                                "Throughput" to performance.throughput,
                                "Lead Time" to performance.leadTime
                            )
                        )
                    }
                    .toMultiAxisChart()
            }
    }

    fun leadTimeCompare(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): MultiAxisChart<Double> {
        if (!boardPreferences.hasMultipleLeadTimeFeatureEnabled) {
            return MultiAxisChart()
        }

        return chartRepository.findLeadTimeComparisonByPeriod(filter)
            .groupBy { boardPreferences.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { it.leadTimeName to it.leadTime } }
            .toMultiAxisChart()
    }

    fun throughputByEstimate(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): MultiAxisChart<Int> {
        if (!boardPreferences.hasEstimateFeatureEnabled) {
            return MultiAxisChart()
        }

        val uninformedValue = messageResolver.resolve("uninformed")
        return chartRepository.findThroughputByPeriodAndEstimate(filter)
            .groupBy { boardPreferences.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { Pair(it.estimate ?: uninformedValue, it.throughput) } }
            .toMultiAxisChart()
    }
}
