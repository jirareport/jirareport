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
class CreateThroughputByEstimateChartUseCase(
    private val chartRepository: ChartRepository,
    private val messageResolver: MessageResolver,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): MultiAxisChart<Int> {
        log.info("Action=CreateThroughputByEstimateChartUseCase, filter={}, boardPreferences={}", filter, boardPreferences)

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
