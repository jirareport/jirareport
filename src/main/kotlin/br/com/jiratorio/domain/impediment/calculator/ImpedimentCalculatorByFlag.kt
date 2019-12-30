package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.time.daysDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

object ImpedimentCalculatorByFlag : ImpedimentCalculator {

    private val log = LoggerFactory.getLogger(javaClass)

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
            return ImpedimentCalculatorResult()
        }

        beginnings.sort()
        terms.sort()

        val impedimentHistory = sortedSetOf<ImpedimentHistory>()
        for (i in terms.indices) {
            val history = ImpedimentHistory(
                startDate = beginnings[i],
                endDate = terms[i],
                leadTime = beginnings[i].daysDiff(terms[i], holidays, ignoreWeekend)
            )

            impedimentHistory.add(history)
        }

        return ImpedimentCalculatorResult(
            timeInImpediment = impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory = impedimentHistory
        )
    }

}
