package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.LeadTimeRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChartByPeriodUseCase(
    private val leadTimeRepository: LeadTimeRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issuePeriods: List<IssuePeriodEntity>, board: BoardEntity): MultiAxisChart<Double> {
        log.info("Action=createLeadTimeCompareChartByPeriod, issuePeriods={}, board={}", issuePeriods, board)

        if (board.leadTimeConfigs.isNullOrEmpty()) {
            return MultiAxisChart()
        }

        return leadTimeRepository.findComparisonByPeriod(issuePeriods.map(IssuePeriodEntity::id))
            .groupBy { board.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { it.leadTimeName to it.leadTime } }
            .toMultiAxisChart()
    }

}
