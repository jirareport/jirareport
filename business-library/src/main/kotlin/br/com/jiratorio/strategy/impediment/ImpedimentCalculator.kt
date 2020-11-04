package br.com.jiratorio.strategy.impediment

import br.com.jiratorio.domain.ImpedimentHistory
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.changelog.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

internal fun interface ImpedimentCalculator {

    fun calcImpediment(
        impedimentColumns: List<String>?,
        changelog: Changelog,
        endDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?,
    ): Pair<Long, Set<ImpedimentHistory>>

    companion object {

        fun from(impedimentType: ImpedimentType?): ImpedimentCalculator =
            when (impedimentType) {
                ImpedimentType.COLUMN -> ImpedimentCalculatorByColumn
                ImpedimentType.FLAG -> ImpedimentCalculatorByFlag
                null -> EMPTY_CALCULATOR
            }

        private val EMPTY_CALCULATOR: ImpedimentCalculator = ImpedimentCalculator { _, _, _, _, _ -> EMPTY_RESULT }

        internal val EMPTY_RESULT: Pair<Long, Set<ImpedimentHistory>> = Pair(0, emptySet())

    }

}
