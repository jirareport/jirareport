package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.IssueTypeChartService
import org.springframework.stereotype.Service

@Service
class IssueTypeChartServiceImpl(
    private val messageResolver: MessageResolver
) : IssueTypeChartService {

    override fun leadTimeByType(issues: List<Issue>): Chart<String, Double> {
        log.info("Method=leadTimeByType, issues={}", issues)

        return issues
            .groupBy { it.issueType ?: messageResolver.resolve("uninformed") }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputByType(issues: List<Issue>): Chart<String, Int> {
        log.info("Method=throughputByType, issues={}", issues)

        return issues
            .groupingBy { it.issueType ?: messageResolver.resolve("uninformed") }
            .eachCount()
            .toChart()
    }

}
