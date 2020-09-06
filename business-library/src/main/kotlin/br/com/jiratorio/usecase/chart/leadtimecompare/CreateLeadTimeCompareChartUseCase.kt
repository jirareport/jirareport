package br.com.jiratorio.usecase.chart.leadtimecompare

import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.mapper.toChart
import br.com.jiratorio.repository.LeadTimeRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChartUseCase(
    private val leadTimeRepository: LeadTimeRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createLeadTimeCompareChart, issues={}", issues)

        if (issues.isEmpty()) {
            return Chart()
        }

        if (issues.first() is IssueEntity) {
            @Suppress("UNCHECKED_CAST")
            return buildLeadTimeCompareChartWithIssueEntity(issues as List<IssueEntity>)
        }

        return leadTimeRepository.findAverageLeadTime(issues.map(Issue::id))
            .associate { it.name to it.value }
            .toChart()
    }

    private fun buildLeadTimeCompareChartWithIssueEntity(issues: List<IssueEntity>): Chart<String, Double> =
        issues
            .mapNotNull { it.leadTimes }
            .flatten()
            .groupBy { it.leadTimeConfig.name }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()

}
