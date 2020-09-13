package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.changelog.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

interface ImpedimentCalculator {

    fun calcImpediment(
        impedimentColumns: List<String>?,
        changelog: Changelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): ImpedimentCalculatorResult

}
