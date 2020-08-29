package br.com.jiratorio.usecase.chart.leadtimecompare

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.mapper.toChart
import org.slf4j.LoggerFactory

@UseCase
class CreateLeadTimeCompareChart {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Chart<String, Double> {
        log.info("Action=createLeadTimeCompareChart, issues={}", issues)

        return issues
            .mapNotNull { it.leadTimes }
            .flatten()
            .groupBy { it.leadTimeConfig.name }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

}
