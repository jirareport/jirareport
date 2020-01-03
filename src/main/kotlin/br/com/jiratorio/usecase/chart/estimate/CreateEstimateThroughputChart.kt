package br.com.jiratorio.usecase.chart.estimate

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateEstimateThroughputChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Int> {
        log.info("Action=createEstimateThroughputChart, issues={}", issues)

        return issues.groupingBy { it.estimate ?: messageResolver("uninformed") }
            .eachCount()
            .toChart()
    }

}
