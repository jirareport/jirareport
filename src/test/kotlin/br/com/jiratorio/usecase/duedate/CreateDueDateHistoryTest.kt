package br.com.jiratorio.usecase.duedate

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.context.UnitTestContext
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.JiraChangelogItemFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.Comparator

@Tag("unit")
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UnitTestContext::class])
internal class CreateDueDateHistoryTest(
    private val jiraChangelogItemFactory: JiraChangelogItemFactory
) {

    private val extractDueDateHistory = CreateDueDateHistory()

    @Test
    fun `extract due date history with one item`() {
        val jiraChangelogItem = jiraChangelogItemFactory.create()
        val dueDateHistories = extractDueDateHistory.execute("duedate", listOf(jiraChangelogItem))

        assertThat(dueDateHistories).hasSize(1)
        dueDateHistories.first().assertThat {
            hasDueDate(jiraChangelogItem.to?.toLocalDate("yyyy-MM-dd"))
            hasCreated(jiraChangelogItem.created)
        }
    }

    @Test
    fun `extract due date history with many items`() {
        jiraChangelogItemFactory.create(
            quantity = 5,
            modifyingFields = mapOf(
                JiraChangelogItem::field to "other_field"
            )
        )

        val jiraChangelogItems = jiraChangelogItemFactory.create(5)

        val dueDateHistories = extractDueDateHistory.execute("duedate", jiraChangelogItems)

        assertThat(dueDateHistories)
            .hasSize(5)
            .isSortedAccordingTo(Comparator.comparing<DueDateHistory, LocalDateTime> {
                it.created
            })
    }

}
