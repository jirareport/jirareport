package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.parsed.ParsedChangelog
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByColumn : ImpedimentCalculator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        parsedChangelog: ParsedChangelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult {
        log.info(
            "Method=timeInImpediment, impedimentColumns={}, parsedChangelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, parsedChangelog, endDate, holidays, ignoreWeekend
        )

        if (impedimentColumns == null || impedimentColumns.isEmpty()) {
            return ImpedimentCalculatorResult()
        }

        val impedimentHistory = parsedChangelog.columnChangelog
            .filter { impedimentColumns.contains(it.to) }
            .map {
                ImpedimentHistory(
                    startDate = it.startDate,
                    endDate = it.endDate,
                    leadTime = it.leadTime
                )
            }

        return ImpedimentCalculatorResult(
            timeInImpediment = impedimentHistory.map { it.leadTime }.sum(),
            impedimentHistory = impedimentHistory
                .sortedWith(Comparator.comparing(ImpedimentHistory::startDate))
                .toMutableSet()
        )
    }

}
