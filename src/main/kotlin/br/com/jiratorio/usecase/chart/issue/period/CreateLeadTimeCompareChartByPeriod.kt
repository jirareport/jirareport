package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.chart.LeadTimeCompareChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChartByPeriod {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issuePeriods: List<IssuePeriod>, board: Board): LeadTimeCompareChart {
        log.info("Action=createLeadTimeCompareChartByPeriod, issuePeriods={}, board={}", issuePeriods, board)

        val leadTimeCompareChart = LeadTimeCompareChart()

        val leadTimeConfigs = board.leadTimeConfigs ?: return leadTimeCompareChart

        for (issuePeriod in issuePeriods) {
            val periodChart = issuePeriod.leadTimeCompareChart ?: continue
            val collect = periodChart.data.toMutableMap()

            if (collect.size < leadTimeConfigs.size) {
                leadTimeConfigs.forEach {
                    if (!collect.containsKey(it.name)) {
                        collect[it.name] = 0.0
                    }
                }
            }

            leadTimeCompareChart.add(issuePeriod.dates, collect)
        }

        return leadTimeCompareChart
    }

}
