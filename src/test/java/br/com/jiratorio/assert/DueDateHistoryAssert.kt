package br.com.jiratorio.assert

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import java.time.LocalDate
import java.time.LocalDateTime

class DueDateHistoryAssert(actual: DueDateHistory) :
        BaseAssert<DueDateHistoryAssert, DueDateHistory>(actual, DueDateHistoryAssert::class) {

    fun hasDueDate(dueDate: LocalDate?) = assertAll {
        objects.assertEqual(field("dueDateHistory.dueDate"), actual.dueDate, dueDate)
    }

    fun hasCreated(created: LocalDateTime?) = assertAll {
        objects.assertEqual(field("dueDateHistory.created"), actual.created, created)
    }

}
