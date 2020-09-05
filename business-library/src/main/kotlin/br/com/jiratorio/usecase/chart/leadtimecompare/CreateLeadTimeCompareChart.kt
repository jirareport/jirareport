package br.com.jiratorio.usecase.chart.leadtimecompare

import br.com.jiratorio.domain.AverageLeadTime
import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import br.com.jiratorio.repository.LeadTimeRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChart(
    private val leadTimeRepository: LeadTimeRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<MinimalIssue>): Chart<String, Double> {
        log.info("Action=createLeadTimeCompareChart, issues={}", issues)

        return leadTimeRepository.findAverageLeadTime(issues.map(MinimalIssue::id))
            .associateBy(AverageLeadTime::name)
            .mapValues { it.value.value }
            .toChart()
    }

}
