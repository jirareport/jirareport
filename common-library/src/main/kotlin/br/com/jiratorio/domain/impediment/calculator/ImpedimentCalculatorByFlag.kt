package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.parsed.ParsedChangelog
import br.com.jiratorio.extension.time.daysDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

object ImpedimentCalculatorByFlag : ImpedimentCalculator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun calcImpediment(
        impedimentColumns: List<String>?,
        parsedChangelog: ParsedChangelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult {
        log.info(
            "Method=calcImpediment, impedimentColumns={}, parsedChangelog={}, endDate={}, holidays={}, ignoreWeekend={}",
            impedimentColumns, parsedChangelog, endDate, holidays, ignoreWeekend
        )

        val beginnings = mutableListOf<LocalDateTime>()
        val terms = mutableListOf<LocalDateTime>()

        parsedChangelog.fieldChangelog
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
            log.info(
                "Method=countTimeInImpedimentByFlag, Info=tamanhos diferentes, beginnings={}, terms={}",
                beginnings.size,
                terms.size
            )
            return ImpedimentCalculatorResult()
        }

        beginnings.sort()
        terms.sort()

        val impedimentHistory = sortedSetOf<ImpedimentHistoryEntity>()
        for (i in terms.indices) {
            val history = ImpedimentHistoryEntity(
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
