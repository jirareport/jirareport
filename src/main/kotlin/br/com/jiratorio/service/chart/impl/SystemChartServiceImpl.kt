package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.SystemChartService
import org.springframework.stereotype.Service
import rx.Single

@Service
class SystemChartServiceImpl : SystemChartService {

    override fun leadTimeBySystemAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Double>> =
        Single.fromCallable {
            log.info("Method=leadTimeBySystem, issues={}", issues)

            issues
                .groupBy { it.system ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputBySystemAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Int>> =
        Single.fromCallable {
            log.info("Method=throughputBySystem, issues={}", issues)

            issues
                .groupingBy { it.system ?: uninformed }
                .eachCount()
                .toChart()
        }

}
