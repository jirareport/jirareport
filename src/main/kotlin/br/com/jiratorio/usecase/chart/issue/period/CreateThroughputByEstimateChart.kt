package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.chart.ThroughputByEstimate
import br.com.jiratorio.domain.entity.IssuePeriod
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.HashSet
import java.util.LinkedHashMap

@Service
class CreateThroughputByEstimateChart {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issuePeriods: List<IssuePeriod>): ThroughputByEstimate {
        log.info("Action=createThroughputByEstimateChart, issuePeriods={}", issuePeriods)

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
