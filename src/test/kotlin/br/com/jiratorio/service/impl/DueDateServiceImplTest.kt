package br.com.jiratorio.service.impl

import br.com.jiratorio.assert.DueDateHistoryAssert
import br.com.jiratorio.context.UnitTestContext
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.JiraChangelogItemFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
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
        val jiraChangelogItem = jiraChangelogItemFactory.create()

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", listOf(jiraChangelogItem))

        assertThat(dueDateHistories).hasSize(1)
        DueDateHistoryAssert(dueDateHistories.first()).assertThat {
            hasDueDate(jiraChangelogItem.to?.toLocalDate("yyyy-MM-dd"))
            hasCreated(jiraChangelogItem.created)
        }
    }

    @Test
    fun `extract due date history with many items`() {
        jiraChangelogItemFactory.create(5) {
            field = "other_field"
        }
        val jiraChangelogItems = jiraChangelogItemFactory.create(5)

        val dueDateHistories = dueDateService.extractDueDateHistory("duedate", jiraChangelogItems)

        assertThat(dueDateHistories)
            .hasSize(5)
            .isSortedAccordingTo(Comparator.comparing<DueDateHistory, LocalDateTime> {
                it.created
            })
    }

}
