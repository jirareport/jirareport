package br.com.jiratorio.usecase.chart.priority

import br.com.jiratorio.internationalization.MessageResolver
 import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreatePriorityThroughputChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createPriorityThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupingBy { it.priority ?: uninformedValue }
            .eachCount()
            .toChart()
    }

}
