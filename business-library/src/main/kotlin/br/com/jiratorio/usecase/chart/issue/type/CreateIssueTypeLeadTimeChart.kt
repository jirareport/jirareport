package br.com.jiratorio.usecase.chart.issue.type

import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateIssueTypeLeadTimeChart(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createIssueTypeLeadTimeChart, issues={}", issues)

        val uninformedValue = messageResolver.resolve("uninformed")
        return issues
            .groupBy { it.issueType ?: uninformedValue }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

}
