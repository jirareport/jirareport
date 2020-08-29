package br.com.jiratorio.domain.duedate.calculator

import br.com.jiratorio.config.junit.testtype.UnitTest
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
internal class FirstAndLastDueDateCalculatorTest {

    @Test
    fun `calc deviation of estimate with due date type first and last due date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val thirdDueDate = DueDateHistory(null, "15/02/2019".toLocalDate())
        val dueDateHistories = listOf(firstDueDate, secondDueDate, thirdDueDate)

        val deviationOfEstimate = FirstAndLastDueDateCalculator.calcDeviationOfEstimate(
            dueDateHistories,
            LocalDateTime.now(),
            true,
            emptyList()
        )

        assertThat(deviationOfEstimate)
            .isEqualTo(11)
    }

}
