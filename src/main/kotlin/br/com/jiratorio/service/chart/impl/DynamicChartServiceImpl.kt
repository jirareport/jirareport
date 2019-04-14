package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.DynamicChartService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class DynamicChartServiceImpl : DynamicChartService {

    override fun buildDynamicChartsAsync(
        issues: List<Issue>,
        board: Board,
        uninformed: String
    ): Deferred<List<DynamicChart>> =
        GlobalScope.async {
            log.info("Method=buildDynamicCharts, issues={}, board={}", issues, board)

            val dynamicFields = board.dynamicFields
            if (dynamicFields.isNullOrEmpty()) {
                emptyList()
            } else {
                dynamicFields.map {
                    DynamicChart(
                        name = it.name,
                        leadTime = buildDynamicLeadTime(it, issues, uninformed),
                        throughput = buildDynamicThroughput(it, issues, uninformed)
                    )
                }
            }
        }

    private fun buildDynamicLeadTime(
        config: DynamicFieldConfig,
        issues: List<Issue>,
        uninformed: String
    ): Chart<String, Double> {
        log.info("Method=buildDynamicLeadTime, config={}, issues={}", config, issues)

        return issues
            .filterNot { it.dynamicFields.isNullOrEmpty() }
            .groupBy { it.dynamicFields!![config.name] ?: uninformed }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    private fun buildDynamicThroughput(
        config: DynamicFieldConfig,
        issues: List<Issue>,
        uninformed: String
    ): Chart<String, Int> {
        log.info("Method=buildDynamicThroughput, config={}, issues={}", config, issues)

        return issues
            .filterNot { it.dynamicFields.isNullOrEmpty() }
            .groupingBy { it.dynamicFields!![config.name] ?: uninformed }
            .eachCount()
            .toChart()
    }

}
