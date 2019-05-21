package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import java.time.LocalDate
import java.time.LocalDateTime

interface ImpedimentCalculator {

    fun calcImpediment(
        impedimentColumns: List<String>?,
        changelogItems: List<JiraChangelogItem>,
        changelog: List<Changelog>,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult

}
