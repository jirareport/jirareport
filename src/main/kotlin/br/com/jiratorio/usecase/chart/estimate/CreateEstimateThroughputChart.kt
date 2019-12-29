package br.com.jiratorio.usecase.chart.estimate

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateEstimateThroughputChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Int> {
        log.info("Method=execute, issues={}", issues)

        return issues.groupingBy { it.estimate ?: messageResolver("uninformed") }
            .eachCount()
            .toChart()
    }

}
