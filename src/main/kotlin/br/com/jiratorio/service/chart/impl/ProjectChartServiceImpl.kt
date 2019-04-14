package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.ProjectChartService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class ProjectChartServiceImpl : ProjectChartService {

    override fun leadTimeByProjectAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Double>> =
        GlobalScope.async {
            log.info("Method=leadTimeByProject, issues={}", issues)

            issues
                .groupBy { it.project ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputByProjectAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Int>> =
        GlobalScope.async {
            log.info("Method=throughputByProject, issues={}", issues)

            issues
                .groupingBy { it.project ?: uninformed }
                .eachCount()
                .toChart()
        }

}
