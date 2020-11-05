package br.com.jiratorio.strategy.duedate

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.time.daysDiff
import java.time.LocalDate
import java.time.LocalDateTime

object FirstDueDateAndEndDateCalculator : DueDateCalculator {

    override fun calcDeviationOfEstimate(dueDateHistories: List<DueDateHistory>, endDate: LocalDateTime, ignoreWeekend: Boolean?, holidays: List<LocalDate>): Long {
        if (dueDateHistories.isEmpty()) {
            return 0
        }

        val first = dueDateHistories.first()
        return first.dueDate.daysDiff(endDate.toLocalDate(), holidays, ignoreWeekend)
    }

}
