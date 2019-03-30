package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.extension.logger
import java.time.LocalDate
import java.time.LocalDateTime

object LastDueDateAndEndDateCalculator : DueDateCalculator {

    private val log = logger()

    override fun calcDeviationOfEstimate(
        dueDateHistories: List<DueDateHistory>,
        endDate: LocalDateTime,
        ignoreWeekend: Boolean?,
        holidays: List<LocalDate>
    ): Long {
        log.info(
            "Method=calcDeviationOfEstimateBetweenLastDueDateAndEndDate, dueDateHistories={}, endDate={}",
            dueDateHistories, endDate
        )

        if (dueDateHistories.isEmpty()) {
            return 0
        }

        val last = dueDateHistories.last()
        return last.dueDate.daysDiff(endDate.toLocalDate(), holidays, ignoreWeekend)
    }

}
