package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.time.daysDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

object FirstDueDateAndEndDateCalculator : DueDateCalculator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun calcDeviationOfEstimate(
        dueDateHistories: List<DueDateHistory>,
        endDate: LocalDateTime,
        ignoreWeekend: Boolean?,
        holidays: List<LocalDate>
    ): Long {
        log.info(
            "Method=calcDeviationOfEstimateBetweenFirstDueDateAndEndDate, dueDateHistories={}, endDate={}, ignoreWeekend={}, holidays={}",
            dueDateHistories, endDate, ignoreWeekend, holidays
        )

        if (dueDateHistories.isEmpty()) {
            return 0
        }

        val first = dueDateHistories.first()
        return first.dueDate.daysDiff(endDate.toLocalDate(), holidays, ignoreWeekend)
    }

}
