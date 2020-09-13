package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.Changelog
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByColumn : ImpedimentCalculator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        changelog: Changelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?,
    ): ImpedimentCalculatorResult {
        log.info(
            "Method=timeInImpediment, impedimentColumns={}, changelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, changelog, endDate, holidays, ignoreWeekend
        )

        if (impedimentColumns == null || impedimentColumns.isEmpty()) {
            return ImpedimentCalculatorResult()
        }

        val impedimentHistory = changelog.columnChangelog
            .filter { impedimentColumns.contains(it.to) }
            .map {
                ImpedimentHistoryEntity(
                    startDate = it.startDate,
                    endDate = it.endDate,
                    leadTime = it.leadTime
                )
            }

        return ImpedimentCalculatorResult(
            timeInImpediment = impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory = impedimentHistory
                .sortedWith(Comparator.comparing(ImpedimentHistoryEntity::startDate))
                .toMutableSet()
        )
    }

}
