package br.com.jiratorio.usecase.chart.estimate

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateEstimateThroughputChartUseCase(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<MinimalIssue>): Chart<String, Int> {
        log.info("Action=createEstimateThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues.groupingBy { it.estimate ?: uninformedValue }
            .eachCount()
            .toChart()
    }

}
