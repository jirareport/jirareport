package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toChart
import br.com.jiratorio.service.chart.EstimateChartService
import org.springframework.stereotype.Service

@Service
class EstimateChartServiceImpl : EstimateChartService {

    override fun leadTimeChartAsync(issues: List<Issue>, uninformed: String): Chart<String, Double> {
        log.info("Method=leadTimeChart, issues={}", issues)

        return issues.groupBy { it.estimate ?: uninformed }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputChartAsync(issues: List<Issue>, uninformed: String): Chart<String, Int> {
        log.info("Method=throughputChart, issues={}", issues)

        return issues.groupingBy { it.estimate ?: uninformed }
            .eachCount()
            .toChart()
    }

}
