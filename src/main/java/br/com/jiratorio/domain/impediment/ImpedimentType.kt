package br.com.jiratorio.domain.impediment

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByColumn
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByFlag
import java.time.LocalDate
import java.time.LocalDateTime

enum class ImpedimentType {

    COLUMN {
        override fun timeInImpediment(impedimentColumns: List<String>, changelogItems: List<JiraChangelogItem>,
                                      changelog: List<Changelog>, endDate: LocalDateTime, holidays: List<LocalDate>,
                                      ignoreWeekend: Boolean?) =
                ImpedimentCalculatorByColumn.timeInImpediment(changelog, impedimentColumns)
    },

    FLAG {
        override fun timeInImpediment(impedimentColumns: List<String>, changelogItems: List<JiraChangelogItem>,
                                      changelog: List<Changelog>, endDate: LocalDateTime, holidays: List<LocalDate>,
                                      ignoreWeekend: Boolean?) =
                ImpedimentCalculatorByFlag.timeInImpediment(changelogItems, endDate, holidays, ignoreWeekend)
    };

    abstract fun timeInImpediment(impedimentColumns: List<String>, changelogItems: List<JiraChangelogItem>,
                                  changelog: List<Changelog>, endDate: LocalDateTime, holidays: List<LocalDate>,
                                  ignoreWeekend: Boolean?): Long

}
