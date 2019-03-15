package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.util.DateUtil
import br.com.jiratorio.util.logger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

object ImpedimentCalculatorByFlag {

    private val log = logger()

    fun timeInImpediment(changelogItems: List<JiraChangelogItem>, endDate: LocalDateTime,
                         holidays: List<LocalDate>, ignoreWeekend: Boolean?): Long {
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
            log.info("Method=countTimeInImpedimentByFlag, Info=tamanhos diferentes, beginnings={}, terms={}", beginnings.size, terms.size)
            return 0
        }

        beginnings.sort()
        terms.sort()

        var timeInImpediment: Long = 0
        for (i in terms.indices) {
            timeInImpediment += DateUtil.daysDiff(beginnings[i], terms[i], holidays, ignoreWeekend)
        }

        return timeInImpediment
    }

}
