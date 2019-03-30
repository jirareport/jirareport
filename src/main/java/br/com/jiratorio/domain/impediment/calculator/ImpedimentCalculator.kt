package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

interface ImpedimentCalculator {

    fun timeInImpediment(
        impedimentColumns: List<String>?,
        changelogItems: List<JiraChangelogItem>,
        changelog: List<Changelog>,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Long

}
