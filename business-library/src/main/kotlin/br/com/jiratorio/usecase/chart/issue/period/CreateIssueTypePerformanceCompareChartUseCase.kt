package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.BoardEntity
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

    fun execute(board: BoardEntity, issuePeriods: List<Long>): Map<String, MultiAxisChart<Number>> {
        log.info("Action=createIssueTypePerformanceCompareChart, issuePeriods={}", issuePeriods)

        val uninformedValue = messageResolver.resolve("uninformed")
        return chartRepository.findPerformanceComparisonByIssueType(board.id, issuePeriods)
            .groupBy { it.issueType ?: uninformedValue }
            .mapValues {
                it.value
                    .associate { performance ->
                        Pair(
                            board.issuePeriodNameFormat.format(performance.periodStart, performance.periodEnd),
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
