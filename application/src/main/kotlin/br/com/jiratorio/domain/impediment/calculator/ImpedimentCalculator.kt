package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.parsed.ParsedChangelog
import java.time.LocalDate
import java.time.LocalDateTime

interface ImpedimentCalculator {

    fun calcImpediment(
        impedimentColumns: List<String>?,
        parsedChangelog: ParsedChangelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult

}
