package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.LeadTimeCompareChartService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import rx.Single

@Service
class LeadTimeCompareChartServiceImpl : LeadTimeCompareChartService {

    override fun leadTimeCompareAsync(issues: List<Issue>): Single<Chart<String, Double>> =
        Single.fromCallable {
            log.info("Method=leadTimeCompare, issues={}", issues)

            issues
                .mapNotNull { it.leadTimes }
                .flatten()
                .groupBy { it.leadTimeConfig.name }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

}
