package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.PriorityChartService
import org.springframework.stereotype.Service

@Service
class PriorityChartServiceImpl : PriorityChartService {

    override fun leadTimeByPriorityAsync(issues: List<Issue>, uninformed: String): Chart<String, Double> {
        log.info("Method=leadTimeByPriority, issues={}", issues)

        return issues
            .groupBy { it.priority ?: uninformed }
            .mapValues { (_, value) -> value.map { it.leadTime }.average() }
            .toChart()
    }

    override fun throughputByPriorityAsync(issues: List<Issue>, uninformed: String): Chart<String, Int> {
        log.info("Method=throughputByPriority, issues={}", issues)

        return issues
            .groupingBy { it.priority ?: uninformed }
            .eachCount()
            .toChart()
    }

}
