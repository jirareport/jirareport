package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.ChartRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateIssueTypePerformanceCompareChartUseCase(
    private val chartRepository: ChartRepository,
    private val messageResolver: MessageResolver,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): Map<String, MultiAxisChart<Number>> {
        log.info("Action=CreateIssueTypePerformanceCompareChartUseCase, filter={}, boardPreferences={}", filter, boardPreferences)

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

}
