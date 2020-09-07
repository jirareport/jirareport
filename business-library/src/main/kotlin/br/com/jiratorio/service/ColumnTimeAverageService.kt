package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.repository.ColumnTimeAverageRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ColumnTimeAverageService(
    private val columnTimeAverageRepository: ColumnTimeAverageRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(issuePeriod: IssuePeriodEntity, fluxColumn: List<String>) {
        log.info("issuePeriod={}, fluxColumn={}", issuePeriod, fluxColumn)

        val issues = issuePeriod.issues

        issues
            .flatMap(IssueEntity::columnChangelog)
            .groupBy(ColumnChangelogEntity::to, ColumnChangelogEntity::leadTime)
            .mapValues { it.value.sum().toDouble() / issues.size }
            .map { (columnName, averageTime) ->
                ColumnTimeAverageEntity(
                    issuePeriodId = issuePeriod.id,
                    columnName = columnName,
                    averageTime = averageTime
                )
            }
            .sortedBy { fluxColumn.indexOf(it.columnName.toUpperCase()) }
            .forEach(columnTimeAverageRepository::save)
    }

}
