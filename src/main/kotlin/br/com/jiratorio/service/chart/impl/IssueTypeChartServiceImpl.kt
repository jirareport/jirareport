package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.toChart
import br.com.jiratorio.service.chart.IssueTypeChartService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import rx.Single

@Service
class IssueTypeChartServiceImpl : IssueTypeChartService {

    override fun leadTimeByTypeAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Double>> =
        Single.fromCallable {
            log.info("Method=leadTimeByType, issues={}", issues)

            issues
                .groupBy { it.issueType ?: uninformed }
                .mapValues { (_, value) -> value.map { it.leadTime }.average() }
                .toChart()
        }

    override fun throughputByTypeAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Int>> =
        Single.fromCallable {
            log.info("Method=throughputByType, issues={}", issues)

            issues
                .groupingBy { it.issueType ?: uninformed }
                .eachCount()
                .toChart()
        }

}
