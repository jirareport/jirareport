package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.SystemChartService
import org.springframework.stereotype.Service

@Service
class SystemChartServiceImpl(
    private val messageResolver: MessageResolver
) : SystemChartService {

    override fun leadTimeBySystem(issues: List<Issue>): Chart<String, Double> {
        log.info("Method=leadTimeBySystem, issues={}", issues)

        return issues
            .groupBy { it.system ?: messageResolver.resolve("uninformed") }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputBySystem(issues: List<Issue>): Chart<String, Int> {
        log.info("Method=throughputBySystem, issues={}", issues)

        return issues
            .groupingBy { it.system ?: messageResolver.resolve("uninformed") }
            .eachCount()
            .toChart()
    }
}
