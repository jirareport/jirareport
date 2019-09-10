package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class LastDueDateAndEndDateCalculatorTest {

    @Test
    fun `calc deviation of estimate with due date type last and end date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val dueDateHistories = listOf(firstDueDate, secondDueDate)
        val endDate = "15/02/2019 09:40".toLocalDateTime()

        val deviationOfEstimate = LastDueDateAndEndDateCalculator.calcDeviationOfEstimate(
            dueDateHistories,
            endDate,
            true,
            emptyList()
        )

        assertThat(deviationOfEstimate)
            .isEqualTo(6)
    }

}
