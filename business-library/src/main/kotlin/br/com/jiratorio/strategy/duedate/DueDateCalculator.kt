package br.com.jiratorio.strategy.duedate

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import java.time.LocalDate
import java.time.LocalDateTime

internal interface DueDateCalculator {

    fun calcDeviationOfEstimate(dueDateHistories: List<DueDateHistory>, endDate: LocalDateTime, ignoreWeekend: Boolean?, holidays: List<LocalDate>): Long

    companion object {

        fun from(dueDateType: DueDateType): DueDateCalculator =
            when (dueDateType) {
                DueDateType.FIRST_AND_LAST_DUE_DATE -> FirstAndLastDueDateCalculator
                DueDateType.FIRST_DUE_DATE_AND_END_DATE -> FirstDueDateAndEndDateCalculator
                DueDateType.LAST_DUE_DATE_AND_END_DATE -> LastDueDateAndEndDateCalculator
            }

    }

}
