package br.com.jiratorio.service.impl

import br.com.jiratorio.context.UnitTestContext
import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.factory.domain.JiraChangelogItemFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays
import java.util.Arrays.asList
import java.util.Comparator

@Tag("unit")
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UnitTestContext::class])
internal class DueDateServiceImplTest @Autowired constructor(
        private val jiraChangelogItemFactory: JiraChangelogItemFactory
) {

    private var dueDateService = DueDateServiceImpl()

    @Test
    fun testExtractDueDateHistoryWithOneItem() {
        val jiraChangelogItem = jiraChangelogItemFactory.build()

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", listOf(jiraChangelogItem))

        assertThat(dueDateHistories).hasSize(1)
        dueDateHistories.first().apply {
            assertThat(dueDate)
                    .isEqualTo(LocalDate.parse(jiraChangelogItem.to, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            assertThat(created)
                    .isEqualTo(jiraChangelogItem.created)
        }
    }

    @Test
    fun testExtractDueDateHistoryWithManyItems() {
        jiraChangelogItemFactory.build(5) { empty -> empty.field = "other_field" }
        val jiraChangelogItems = jiraChangelogItemFactory.build(5)

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", jiraChangelogItems)

        assertThat(dueDateHistories)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparing(DueDateHistory::getCreated))
    }

    @Test
    fun testCalcDeviationOfEstimateWithEmptyHistory() {
        val deviationOfEstimate: Long? =
                dueDateService.calcDeviationOfEstimate(null, null, null, null, null)
        assertThat(deviationOfEstimate).isNull()
    }

    @Test
    fun testCalcDeviationOfEstimateWithDueDateTypeFirstAndLastDueDate() {
        val firstDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 5))
        val secondDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 10))
        val thirdDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 15))
        val dueDateHistories = asList(firstDueDate, secondDueDate, thirdDueDate)

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, null, DueDateType.FIRST_AND_LAST_DUE_DATE, true, null)

        assertThat(deviationOfEstimate).isEqualTo(11)
    }

    @Test
    fun testCalcDeviationOfEstimateWithDueDateTypeFirstAndEndDate() {
        val firstDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 5))
        val secondDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 10))
        val dueDateHistories = Arrays.asList(firstDueDate, secondDueDate)
        val endDate = LocalDateTime.of(2019, 2, 13, 13, 30)

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.FIRST_DUE_DATE_AND_END_DATE, true, null)

        assertThat(deviationOfEstimate).isEqualTo(9)
    }

    @Test
    fun testCalcDeviationOfEstimateWithDueDateTypeLastAndEndDate() {
        val firstDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 5))
        val secondDueDate = DueDateHistory(null, LocalDate.of(2019, 2, 10))
        val dueDateHistories = Arrays.asList(firstDueDate, secondDueDate)
        val endDate = LocalDateTime.of(2019, 2, 15, 9, 40)

        val deviationOfEstimate = dueDateService.calcDeviationOfEstimate(dueDateHistories, endDate,
                DueDateType.LAST_DUE_DATE_AND_END_DATE, true, null)

        assertThat(deviationOfEstimate).isEqualTo(6)
    }
}
