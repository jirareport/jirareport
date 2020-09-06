package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.ChartRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChartByPeriodUseCase(
    private val chartRepository: ChartRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(filter: FindAllIssuePeriodsFilter, boardPreferences: BoardPreferences): MultiAxisChart<Double> {
        log.info("Action=createLeadTimeCompareChartByPeriod, filter={}, boardPreferences={}", filter, boardPreferences)

        if (!boardPreferences.hasMultipleLeadTimeFeatureEnabled) {
            return MultiAxisChart()
        }

        return chartRepository.findLeadTimeComparisonByPeriod(filter)
            .groupBy { boardPreferences.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { it.leadTimeName to it.leadTime } }
            .toMultiAxisChart()
    }

}
