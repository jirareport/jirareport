package br.com.jiratorio.usecase.columntimeaverage

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Changelog
import org.slf4j.LoggerFactory

@UseCase
class CalculateColumnTimeAverages {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: Collection<Issue>, fluxColumn: List<String>): Map<String, Double> {
        log.info("Action=calculateColumnTimeAverages, issues={}, fluxColumn={}", issues, fluxColumn)

        return issues
            .flatMap(Issue::changelog)
            .mapNotNull(this::changelogToPair)
            .sortedBy { (to, _) -> fluxColumn.indexOf(to.toUpperCase()) }
            .groupBy(Pair<String, Long>::first)
            .mapValues { (_, values) -> calculateLeadTimeAverage(values, issues) }
    }

    private fun calculateLeadTimeAverage(values: List<Pair<String, Long>>, issues: Collection<Issue>) =
        values.map { (_, leadTime) -> leadTime }
            .sum().toDouble() / issues.size

    private fun changelogToPair(changelog: Changelog): Pair<String, Long>? =
        if (changelog.to == null)
            null
        else
            changelog.to to changelog.leadTime

}
