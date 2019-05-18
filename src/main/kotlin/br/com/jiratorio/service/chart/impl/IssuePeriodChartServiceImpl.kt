package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.chart.ThroughputByEstimate
import br.com.jiratorio.domain.chart.LeadTimeCompareChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.chart.IssuePeriodChartService
import org.springframework.stereotype.Service
import java.util.HashSet
import java.util.LinkedHashMap

@Service
class IssuePeriodChartServiceImpl : IssuePeriodChartService {

    override fun leadTimeCompareByPeriod(issuePeriods: List<IssuePeriod>, board: Board): LeadTimeCompareChart {
        log.info("Method=leadTimeCompareByPeriod, issuePeriods={}, board={}", issuePeriods, board)

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

    override fun throughputByEstimate(issuePeriods: List<IssuePeriod>): ThroughputByEstimate {
        log.info("Method=throughputByEstimate, issuePeriods={}", issuePeriods)

        val sizes = HashSet<String>()
        val periodsSize: MutableMap<String, MutableMap<String, Int>> = LinkedHashMap()
        for (issuePeriod in issuePeriods) {
            val throughputByEstimate = issuePeriod.throughputByEstimate ?: continue
            val estimated = throughputByEstimate.data.toMutableMap()
            sizes.addAll(estimated.keys)
            periodsSize[issuePeriod.dates] = estimated
        }

        periodsSize.forEach { (_, v) ->
            for (size in sizes) {
                if (!v.containsKey(size)) {
                    v[size] = 0
                }
            }
        }

        val datasources = LinkedHashMap<String, MutableList<Int>>()
        for (periodSize in periodsSize.values) {
            periodSize.forEach { (k, v) ->
                val longs = datasources[k] ?: mutableListOf()
                longs.add(v)
                datasources[k] = longs
            }
        }

        return ThroughputByEstimate(periodsSize.keys, datasources)
    }
}
