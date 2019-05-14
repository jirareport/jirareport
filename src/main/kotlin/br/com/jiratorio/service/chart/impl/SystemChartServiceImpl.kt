package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.SystemChartService
import org.springframework.stereotype.Service

@Service
class SystemChartServiceImpl : SystemChartService {

    override fun leadTimeBySystemAsync(issues: List<Issue>, uninformed: String): Chart<String, Double> {
        log.info("Method=leadTimeBySystem, issues={}", issues)

        return issues
            .groupBy { it.system ?: uninformed }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputBySystemAsync(issues: List<Issue>, uninformed: String): Chart<String, Int> {
        log.info("Method=throughputBySystem, issues={}", issues)

        return issues
            .groupingBy { it.system ?: uninformed }
            .eachCount()
            .toChart()
    }

}
