package br.com.jiratorio.usecase.chart.columntime

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import org.slf4j.LoggerFactory

@UseCase
class CreateColumnTimeChart {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>, fluxColumn: List<String>): List<ColumnTimeAvg> {
        log.info("Action=createColumnTimeChart, issues={}, fluxColumn={}", issues, fluxColumn)

        return issues.asSequence()
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
