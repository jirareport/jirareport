package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.logger
import br.com.jiratorio.util.DateUtil
import java.time.LocalDate
import java.time.LocalDateTime

object FirstAndLastDueDateCalculator : DueDateCalculator {

    private val log = logger()

    override fun calcDeviationOfEstimate(
        dueDateHistories: List<DueDateHistory>,
        endDate: LocalDateTime,
        ignoreWeekend: Boolean?,
        holidays: List<LocalDate>?
    ): Long {
        log.info(
            "Method=calcDeviationOfEstimateBetweenFirstAndLastDueDate, dueDateHistories={}, ignoreWeekend={}, holidays={}",
            dueDateHistories, ignoreWeekend, holidays
        )

        val first = dueDateHistories.first()
        val last = dueDateHistories.last()

        return DateUtil.daysDiff(first.dueDate, last.dueDate, holidays, ignoreWeekend)
    }

}
