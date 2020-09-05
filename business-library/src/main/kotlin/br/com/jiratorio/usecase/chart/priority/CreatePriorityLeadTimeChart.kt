package br.com.jiratorio.usecase.chart.priority

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreatePriorityLeadTimeChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<MinimalIssue>): Chart<String, Double> {
        log.info("Action=createPriorityLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.priority ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

}
