package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.chart.ColumnTimeChartService
import org.springframework.stereotype.Service
import rx.Single

@Service
class ColumnTimeChartServiceImpl : ColumnTimeChartService {

    override fun averageAsync(issues: List<Issue>, fluxColumn: List<String>): Single<List<ColumnTimeAvg>> =
        Single.fromCallable {
            log.info("Method=average, issues={}, fluxColumn={}", issues, fluxColumn)

            issues.asSequence()
                .map { it.changelog }
                .flatten()
                .filter { it.to != null }
                .groupBy { it.to!! }
                .mapValues { (_, value) -> value.map { it.leadTime }.sum().toDouble() / issues.size }
                .map { (k, v) -> ColumnTimeAvg(k, v) }
                .sortedWith(Comparator.comparingInt {
                    fluxColumn.indexOf(it.columnName.toUpperCase())
                })
        }

}
