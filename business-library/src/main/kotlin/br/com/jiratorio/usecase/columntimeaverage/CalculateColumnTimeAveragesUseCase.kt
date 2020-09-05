package br.com.jiratorio.usecase.columntimeaverage

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.IssueEntity
import org.slf4j.LoggerFactory

@UseCase
class CalculateColumnTimeAveragesUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: Collection<IssueEntity>, fluxColumn: List<String>): Map<String, Double> {
        log.info("Action=calculateColumnTimeAverages, issues={}, fluxColumn={}", issues, fluxColumn)

        return issues
            .flatMap(IssueEntity::columnChangelog)
            .map { it.to to it.leadTime }
            .sortedBy { (to, _) -> fluxColumn.indexOf(to.toUpperCase()) }
            .groupBy(Pair<String, Long>::first)
            .mapValues { (_, values) -> calculateLeadTimeAverage(values, issues) }
    }

    private fun calculateLeadTimeAverage(values: List<Pair<String, Long>>, issues: Collection<IssueEntity>) =
        values.map { (_, leadTime) -> leadTime }
            .sum().toDouble() / issues.size

}
