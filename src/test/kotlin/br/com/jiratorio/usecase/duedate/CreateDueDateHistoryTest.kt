package br.com.jiratorio.usecase.duedate

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.context.UnitTestContext
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.FieldChangelogFactory
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
    private val fieldChangelogFactory: FieldChangelogFactory
) {

    private val extractDueDateHistory = CreateDueDateHistory()

    @Test
    fun `extract due date history with one item`() {
        val fieldChangelog = fieldChangelogFactory.create()
        val dueDateHistories = extractDueDateHistory.execute("duedate", setOf(fieldChangelog))

        assertThat(dueDateHistories).hasSize(1)
        dueDateHistories.first().assertThat {
            hasDueDate(fieldChangelog.to?.toLocalDate("yyyy-MM-dd"))
            hasCreated(fieldChangelog.created)
        }
    }

    @Test
    fun `extract due date history with many items`() {
        fieldChangelogFactory.create(
            quantity = 5,
            modifyingFields = mapOf(
                FieldChangelog::field to "other_field"
            )
        )

        val fieldChangelog = fieldChangelogFactory.create(5).toSet()

        val dueDateHistories = extractDueDateHistory.execute("duedate", fieldChangelog)

        assertThat(dueDateHistories)
            .hasSize(5)
            .isSortedAccordingTo(Comparator.comparing<DueDateHistory, LocalDateTime>(DueDateHistory::created))
    }

}
