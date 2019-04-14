package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.EstimateChartService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class EstimateChartServiceImpl : EstimateChartService {

    override fun leadTimeChartAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Double>> =
        GlobalScope.async {
            log.info("Method=leadTimeChart, issues={}", issues)

            issues.groupBy { it.estimate ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputChartAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Int>> =
        GlobalScope.async {
            log.info("Method=throughputChart, issues={}", issues)

            issues.groupingBy { it.estimate ?: uninformed }
                .eachCount()
                .toChart()
        }

}
