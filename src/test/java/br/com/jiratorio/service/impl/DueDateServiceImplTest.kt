package br.com.jiratorio.service.impl

import br.com.jiratorio.assert.DueDateHistoryAssert
import br.com.jiratorio.context.UnitTestContext
import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.domain.JiraChangelogItemFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Arrays
import java.util.Arrays.asList
import java.util.Comparator

@Tag("unit")
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UnitTestContext::class])
internal class DueDateServiceImplTest @Autowired constructor(
        private val jiraChangelogItemFactory: JiraChangelogItemFactory
) {

    private val dueDateService = DueDateServiceImpl()

    @Test
    fun `extract due date history with one item`() {
        val jiraChangelogItem = jiraChangelogItemFactory.build()

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", listOf(jiraChangelogItem))

        assertThat(dueDateHistories).hasSize(1)
        DueDateHistoryAssert(dueDateHistories.first()).assertThat {
            hasDueDate(jiraChangelogItem.to?.toLocalDate("yyyy-MM-dd"))
            hasCreated(jiraChangelogItem.created)
        }
    }

    @Test
    fun `extract due date history with many items`() {
        jiraChangelogItemFactory.build(5) { it.field = "other_field" }
        val jiraChangelogItems = jiraChangelogItemFactory.build(5)

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", jiraChangelogItems)

        assertThat(dueDateHistories)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparing(DueDateHistory::getCreated))
    }

    @Test
    fun `calc deviation of estimate with empty history`() {
        val deviationOfEstimate: Long? =
                dueDateService.calcDeviationOfEstimate(null, null, null, null, null)

        assertThat(deviationOfEstimate)
                .isNull()
    }

    @Test
    fun `calc deviation of estimate with due date type first and last due date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val thirdDueDate = DueDateHistory(null, "15/02/2019".toLocalDate())
        val dueDateHistories = asList(firstDueDate, secondDueDate, thirdDueDate)

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, null, DueDateType.FIRST_AND_LAST_DUE_DATE, true, null)

        assertThat(deviationOfEstimate)
                .isEqualTo(11)
    }

    @Test
    fun `calc deviation of estimate with due date type first and end date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val dueDateHistories = Arrays.asList(firstDueDate, secondDueDate)
        val endDate = "13/02/2019 13:30".toLocalDateTime()

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.FIRST_DUE_DATE_AND_END_DATE, true, null)

        assertThat(deviationOfEstimate)
                .isEqualTo(9)
    }

    @Test
    fun `calc deviation of estimate with due date type last and end date`() {
        val firstDueDate = DueDateHistory(null, "05/02/2019".toLocalDate())
        val secondDueDate = DueDateHistory(null, "10/02/2019".toLocalDate())
        val dueDateHistories = Arrays.asList(firstDueDate, secondDueDate)
        val endDate = "15/02/2019 09:40".toLocalDateTime()

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.LAST_DUE_DATE_AND_END_DATE, true, null)

        assertThat(deviationOfEstimate)
                .isEqualTo(6)
    }
}
