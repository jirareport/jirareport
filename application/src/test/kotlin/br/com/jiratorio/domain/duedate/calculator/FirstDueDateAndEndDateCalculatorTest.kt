package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.config.junit.testtype.UnitTest
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@UnitTest
internal class FirstDueDateAndEndDateCalculatorTest {

    @Test
    fun `calc deviation of estimate with due date type first and end date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val dueDateHistories = listOf(firstDueDate, secondDueDate)
        val endDate = "13/02/2019 13:30".toLocalDateTime()

        val deviationOfEstimate = FirstDueDateAndEndDateCalculator.calcDeviationOfEstimate(
            dueDateHistories, endDate,
            true,
            emptyList()
        )

        assertThat(deviationOfEstimate)
            .isEqualTo(9)
    }
}
