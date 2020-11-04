package br.com.jiratorio.strategy.impediment

import br.com.jiratorio.domain.ImpedimentHistory
import br.com.jiratorio.domain.changelog.Changelog
import br.com.jiratorio.extension.time.daysDiff
import java.time.LocalDate
import java.time.LocalDateTime

internal object ImpedimentCalculatorByFlag : ImpedimentCalculator {

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        changelog: Changelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?,
    ): Pair<Long, Set<ImpedimentHistory>> {
        val beginnings = mutableListOf<LocalDateTime>()
        val terms = mutableListOf<LocalDateTime>()

        changelog.fieldChangelog
            .filter { it.field.equals("flagged", ignoreCase = true) }
            .forEach {
                if (it.to.equals("impediment", ignoreCase = true)) {
                    beginnings.add(it.created)
                } else {
                    terms.add(it.created)
                }
            }

        if (beginnings.size - 1 == terms.size) {
            terms.add(endDate)
        }

        if (beginnings.size != terms.size) {
            return ImpedimentCalculator.EMPTY_RESULT
        }

        beginnings.sort()
        terms.sort()

        val impedimentHistory = sortedSetOf<ImpedimentHistory>()
        for (i in terms.indices) {
            val history = InternalImpedimentHistory(
                startDate = beginnings[i],
                endDate = terms[i],
                leadTime = beginnings[i].daysDiff(terms[i], holidays, ignoreWeekend)
            )

            impedimentHistory.add(history)
        }

        return Pair(
            impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory
        )
    }

}
