package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.logger
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByColumn : ImpedimentCalculator {

    private val log = logger()

    override fun timeInImpediment(
        impedimentColumns: List<String>?,
        changelogItems: List<JiraChangelogItem>,
        changelog: List<Changelog>,
        endDate: LocalDateTime,
        holidays: List<LocalDate>?,
        ignoreWeekend: Boolean?
    ): Long {
        log.info(
            "Method=timeInImpediment, impedimentColumns={}, changelogItems={}, changelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, changelogItems, changelog, endDate, holidays, ignoreWeekend
        )

        if (impedimentColumns == null || impedimentColumns.isEmpty()) {
            return 0
        }

        return changelog
            .filter { impedimentColumns.contains(it.to) }
            .map { it.leadTime }
            .sum()
    }

}
