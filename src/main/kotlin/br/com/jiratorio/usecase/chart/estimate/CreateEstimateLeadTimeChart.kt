package br.com.jiratorio.usecase.chart.estimate

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateEstimateLeadTimeChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Double> {
        log.info("Method=leadTimeChart, issues={}", issues)

        return issues.groupBy { it.estimate ?: messageResolver("uninformed") }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

}
