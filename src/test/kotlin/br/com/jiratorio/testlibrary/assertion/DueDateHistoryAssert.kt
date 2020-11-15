package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import org.assertj.core.api.AbstractAssert
import java.time.LocalDate
import java.time.LocalDateTime

class DueDateHistoryAssert(
    actual: DueDateHistory,
) : AbstractAssert<DueDateHistoryAssert, DueDateHistory>(
    actual,
    DueDateHistoryAssert::class.java
) {

    fun hasDueDate(dueDate: LocalDate?): DueDateHistoryAssert {
        if (actual.dueDate != dueDate) {
            failWithMessage(shouldBeEquals(actual.dueDate, dueDate).create())
        }

        return this
    }

    fun hasCreated(created: LocalDateTime?): DueDateHistoryAssert {
        if (actual.created != created) {
            failWithMessage(shouldBeEquals(actual.created, created).create())
        }

        return this
    }

    companion object {
        fun assertThat(actual: DueDateHistory): DueDateHistoryAssert =
            DueDateHistoryAssert(actual)
    }

}
