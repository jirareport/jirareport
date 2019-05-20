package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.log
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByColumn : ImpedimentCalculator {

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        changelogItems: List<JiraChangelogItem>,
        changelog: List<Changelog>,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult {
        log.info(
            "Method=timeInImpediment, impedimentColumns={}, changelogItems={}, changelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, changelogItems, changelog, endDate, holidays, ignoreWeekend
        )

        if (impedimentColumns == null || impedimentColumns.isEmpty()) {
            return ImpedimentCalculatorResult()
        }

        val impedimentHistory = changelog
            .filter { impedimentColumns.contains(it.to) }
            .map {
                ImpedimentHistory(
                    startDate = it.created,
                    endDate = it.endDate,
                    leadTime = it.leadTime
                )
            }

        return ImpedimentCalculatorResult(
            timeInImpediment = impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory = impedimentHistory.toSortedSet()
        )
    }

}
