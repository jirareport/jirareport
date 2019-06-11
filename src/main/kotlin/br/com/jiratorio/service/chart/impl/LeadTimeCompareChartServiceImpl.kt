package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toChart
import br.com.jiratorio.service.chart.LeadTimeCompareChartService
import org.springframework.stereotype.Service

@Service
class LeadTimeCompareChartServiceImpl : LeadTimeCompareChartService {

    override fun leadTimeCompareAsync(issues: List<Issue>): Chart<String, Double> {
        log.info("Method=leadTimeCompare, issues={}", issues)

        return issues
            .mapNotNull { it.leadTimes }
            .flatten()
            .groupBy { it.leadTimeConfig.name }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

}
