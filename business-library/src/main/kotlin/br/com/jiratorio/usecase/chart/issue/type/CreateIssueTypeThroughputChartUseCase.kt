package br.com.jiratorio.usecase.chart.issue.type

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateIssueTypeThroughputChartUseCase(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<MinimalIssue>): Chart<String, Int> {
        log.info("Action=createIssueTypeThroughputChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")

        return issues
            .groupingBy { it.issueType ?: uninformedValue }
            .eachCount()
            .toChart()
    }

}
