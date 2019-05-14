package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.EstimateChartService
import org.springframework.stereotype.Service
import rx.Single

@Service
class EstimateChartServiceImpl : EstimateChartService {

    override fun leadTimeChartAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Double>> =
        Single.fromCallable {
            log.info("Method=leadTimeChart, issues={}", issues)

            issues.groupBy { it.estimate ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputChartAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Int>> =
        Single.fromCallable {
            log.info("Method=throughputChart, issues={}", issues)

            issues.groupingBy { it.estimate ?: uninformed }
                .eachCount()
                .toChart()
        }

}
