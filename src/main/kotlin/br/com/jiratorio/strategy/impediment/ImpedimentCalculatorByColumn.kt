package br.com.jiratorio.strategy.impediment

import br.com.jiratorio.domain.ImpedimentHistory
import br.com.jiratorio.domain.changelog.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByColumn : ImpedimentCalculator {

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        changelog: Changelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?,
    ): Pair<Long, Set<ImpedimentHistory>> {
        if (impedimentColumns == null || impedimentColumns.isEmpty()) {
            return ImpedimentCalculator.EMPTY_RESULT
        }

        val impedimentHistory = changelog.columnChangelog
            .filter { impedimentColumns.contains(it.to) }
            .map {
                InternalImpedimentHistory(
                    startDate = it.startDate,
                    endDate = it.endDate,
                    leadTime = it.leadTime
                )
            }

        return Pair(
            impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory
                .sortedWith(Comparator.comparing(ImpedimentHistory::startDate))
                .toMutableSet()
        )
    }

}
