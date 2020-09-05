package br.com.jiratorio.usecase.columntimeaverage

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.repository.ColumnTimeAverageRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateColumnTimeAverages(
    private val calculateColumnTimeAverages: CalculateColumnTimeAverages,
    private val columnTimeAverageRepository: ColumnTimeAverageRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(issuePeriod: IssuePeriodEntity, fluxColumn: List<String>) {
        log.info("Action=createColumnTimeAverages, issuePeriod={}, fluxColumn={}", issuePeriod, fluxColumn)

        calculateColumnTimeAverages
            .execute(issuePeriod.issues, fluxColumn)
            .map { (columnName, averageTime) ->
                ColumnTimeAverageEntity(
                    issuePeriodId = issuePeriod.id,
                    columnName = columnName,
                    averageTime = averageTime
                )
            }
            .forEach { columnTimeAverageRepository.save(it) }
    }

}
