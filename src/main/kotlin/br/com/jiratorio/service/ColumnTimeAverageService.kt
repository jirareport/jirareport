package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.domain.response.ColumnTimeAverageResponse
import br.com.jiratorio.repository.ColumnTimeAverageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ColumnTimeAverageService(
    private val columnTimeAverageRepository: ColumnTimeAverageRepository,
) {

    @Transactional
    fun create(issuePeriod: IssuePeriodEntity, fluxColumn: List<String>) {
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

    fun retrieveColumnTimeAverages(board: BoardEntity, issues: List<Issue>): List<ColumnTimeAverageResponse> {
        val fluxColumn = board.fluxColumn ?: emptyList()

        return columnTimeAverageRepository.findColumnTimeAverage(issues.map { it.id })
            .sortedBy { (to, _) -> fluxColumn.indexOf(to.toUpperCase()) }
    }

}
