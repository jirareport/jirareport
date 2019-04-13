package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.DynamicChartService
import org.springframework.stereotype.Service

@Service
class DynamicChartServiceImpl(
    private val messageResolver: MessageResolver
) : DynamicChartService {

    override fun buildDynamicCharts(issues: List<Issue>, board: Board): List<DynamicChart> {
        log.info("Method=buildDynamicCharts, issues={}, board={}", issues, board)

        val dynamicFields = board.dynamicFields
        if (dynamicFields.isNullOrEmpty()) {
            return emptyList()
        }

        return dynamicFields.map {
            DynamicChart(
                name = it.name,
                leadTime = buildDynamicLeadTime(it, issues),
                throughput = buildDynamicThroughput(it, issues)
            )
        }
    }

    private fun buildDynamicLeadTime(config: DynamicFieldConfig, issues: List<Issue>): Chart<String, Double> {
        log.info("Method=buildDynamicLeadTime, config={}, issues={}", config, issues)

        return issues
            .filterNot { it.dynamicFields.isNullOrEmpty() }
            .groupBy { it.dynamicFields!![config.name] ?: messageResolver.resolve("uninformed") }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun buildDynamicThroughput(config: DynamicFieldConfig, issues: List<Issue>): Chart<String, Int> {
        log.info("Method=buildDynamicThroughput, config={}, issues={}", config, issues)

        return issues
            .filterNot { it.dynamicFields.isNullOrEmpty() }
            .groupingBy { it.dynamicFields!![config.name] ?: messageResolver.resolve("uninformed") }
            .eachCount()
            .toChart()
    }

}
