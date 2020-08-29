package br.com.jiratorio.usecase.duedate

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.junit.testtype.UnitTest
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.FieldChangelogFactory
import com.github.javafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Comparator

@UnitTest
internal class CreateDueDateHistoryTest {

    private val fieldChangelogFactory = FieldChangelogFactory(Faker())

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
