package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.EstimateChartService
import org.springframework.stereotype.Service

@Service
class EstimateChartServiceImpl(
    private val messageResolver: MessageResolver
) : EstimateChartService {

    override fun leadTimeChart(issues: List<Issue>): Chart<String, Double> {
        log.info("Method=leadTimeChart, issues={}", issues)

        return issues.groupBy { it.estimate ?: messageResolver.resolve("uninformed") }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputChart(issues: List<Issue>): Chart<String, Int> {
        log.info("Method=throughputChart, issues={}", issues)

        return issues.groupingBy { it.estimate ?: messageResolver.resolve("uninformed") }
            .eachCount()
            .toChart()
    }

}
