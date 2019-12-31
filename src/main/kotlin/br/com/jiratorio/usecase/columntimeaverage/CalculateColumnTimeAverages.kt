package br.com.jiratorio.usecase.columntimeaverage

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import org.slf4j.LoggerFactory
import java.util.SortedMap

@UseCase
class CalculateColumnTimeAverages {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: Collection<Issue>, fluxColumn: List<String>): SortedMap<String, Double> {
        log.info("Action=buildColumnTimeAverages, issues={}, fluxColumn={}", issues, fluxColumn)

        return issues.asSequence()
            .map { it.changelog }
            .flatten()
            .filter { it.to != null }
            .groupBy { it.to!! }
            .mapValues { (_, value) -> value.map { it.leadTime }.sum().toDouble() / issues.size }
            .toSortedMap(Comparator.comparingInt { columnName ->
                fluxColumn.indexOf(columnName.toUpperCase())
            })
    }

}
