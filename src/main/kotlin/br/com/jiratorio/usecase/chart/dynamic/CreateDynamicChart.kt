package br.com.jiratorio.usecase.chart.dynamic

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateDynamicChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>, board: Board): List<DynamicChart> {
        log.info("Action=createDynamicChart, issues={}, board={}", issues, board)

        val dynamicFields = board.dynamicFields
        return if (dynamicFields.isNullOrEmpty()) {
            emptyList()
        } else {
            val uninformedValue = messageResolver.resolve("uninformed")

            dynamicFields.map {
                DynamicChart(
                    name = it.name,
                    leadTime = buildDynamicLeadTime(it, issues, uninformedValue),
                    throughput = buildDynamicThroughput(it, issues, uninformedValue)
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
