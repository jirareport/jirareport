package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.SystemChartService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class SystemChartServiceImpl : SystemChartService {

    override fun leadTimeBySystemAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Double>> =
        GlobalScope.async {
            log.info("Method=leadTimeBySystem, issues={}", issues)

            issues
                .groupBy { it.system ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputBySystemAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Int>> =
        GlobalScope.async {
            log.info("Method=throughputBySystem, issues={}", issues)

            issues
                .groupingBy { it.system ?: uninformed }
                .eachCount()
                .toChart()
        }
}
