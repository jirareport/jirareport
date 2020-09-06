package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.ChartRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChartByPeriodUseCase(
    private val chartRepository: ChartRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(board: BoardEntity, issuePeriods: List<Long>): MultiAxisChart<Double> {
        log.info("Action=createLeadTimeCompareChartByPeriod, issuePeriods={}, board={}", issuePeriods, board)

        if (board.leadTimeConfigs.isNullOrEmpty()) {
            return MultiAxisChart()
        }

        return chartRepository.findLeadTimeComparisonByPeriod(issuePeriods)
            .groupBy { board.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { it.leadTimeName to it.leadTime } }
            .toMultiAxisChart()
    }

}
