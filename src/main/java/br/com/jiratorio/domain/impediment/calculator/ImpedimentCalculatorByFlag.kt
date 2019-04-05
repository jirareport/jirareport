package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.daysDiff
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

object ImpedimentCalculatorByFlag : ImpedimentCalculator {

    override fun timeInImpediment(
        impedimentColumns: List<String>?,
        changelogItems: List<JiraChangelogItem>,
        changelog: List<Changelog>,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Long {
        log.info(
            "Method=timeInImpediment, impedimentColumns={}, changelogItems={}, changelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, changelogItems, changelog, endDate, holidays, ignoreWeekend
        )

        val beginnings = ArrayList<LocalDateTime>()
        val terms = ArrayList<LocalDateTime>()

        changelogItems
            .filter { it.field?.equals("flagged", ignoreCase = true) == true }
            .forEach {
                if (it.toString?.equals("impediment", ignoreCase = true) == true) {
                    beginnings.add(it.created!!)
                } else {
                    terms.add(it.created!!)
                }
            }

        if (beginnings.size - 1 == terms.size) {
            terms.add(endDate)
        }

        if (beginnings.size != terms.size) {
            log.info(
                "Method=countTimeInImpedimentByFlag, Info=tamanhos diferentes, beginnings={}, terms={}",
                beginnings.size,
                terms.size
            )
            return 0
        }

        beginnings.sort()
        terms.sort()

        var timeInImpediment: Long = 0
        for (i in terms.indices) {
            timeInImpediment += beginnings[i].daysDiff(terms[i], holidays, ignoreWeekend)
        }

        return timeInImpediment
    }

}
