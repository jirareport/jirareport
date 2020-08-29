package br.com.jiratorio.assertion

import br.com.jiratorio.domain.duedate.DueDateType
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.impediment.ImpedimentType

class BoardAssert(
    actual: Board
) : BaseAssert<BoardAssert, Board>(actual, BoardAssert::class) {

    fun hasName(name: String?) = assertAll {
        objects.assertEqual(field("board.name"), actual.name, name)
    }

    fun hasExternalId(externalId: Long?) = assertAll {
        objects.assertEqual(field("board.externalId"), actual.externalId, externalId)
    }

    fun hasOwner(owner: String) = assertAll {
        objects.assertEqual(field("board.owner"), actual.owner, owner)
    }

    fun hasStartColumn(startColumn: String?) = assertAll {
        val field = field("board.startColumn")
        strings.assertUpperCase(field, actual.startColumn)
        objects.assertEqual(field, actual.startColumn, startColumn?.toUpperCase())
    }

    fun hasEndColumn(endColumn: String?) = assertAll {
        val field = field("board.endColumn")
        strings.assertUpperCase(field, actual.endColumn)
        objects.assertEqual(field, actual.endColumn, endColumn?.toUpperCase())
    }

    fun hasFluxColumn(fluxColumn: List<String>?) = assertAll {
        objects.assertEqual(field("board.fluxColumn"), actual.fluxColumn, fluxColumn?.map { it.toUpperCase() })
    }

    fun hasTouchingColumns(touchingColumns: List<String>?) = assertAll {
        iterables.assertContainsAll(
            field("board.touchingColumns"),
            actual.touchingColumns,
            touchingColumns?.map { it.toUpperCase() }
        )
    }

    fun hasWaitingColumns(waitingColumns: List<String>?) = assertAll {
        iterables.assertContainsAll(
            field("board.waitingColumns"),
            actual.waitingColumns,
            waitingColumns?.map { it.toUpperCase() }
        )
    }

    fun hasIgnoreIssueType(ignoreIssueType: List<String>?) = assertAll {
        iterables.assertContainsAll(field("board.ignoreIssueType"), actual.ignoreIssueType, ignoreIssueType)
    }

    fun hasEpicCF(epicCF: String?) = assertAll {
        objects.assertEqual(field("board.epicCF"), actual.epicCF, epicCF)
    }

    fun hasEstimateCF(estimateCF: String?) = assertAll {
        objects.assertEqual(field("board.estimateCF"), actual.estimateCF, estimateCF)
    }

    fun hasSystemCF(systemCF: String?) = assertAll {
        objects.assertEqual(field("board.systemCF"), actual.systemCF, systemCF)
    }

    fun hasProjectCF(projectCF: String?) = assertAll {
        objects.assertEqual(field("board.projectCF"), actual.projectCF, projectCF)
    }

    fun hasDueDateCF(dueDateCF: String?) = assertAll {
        objects.assertEqual(field("board.dueDateCF"), actual.dueDateCF, dueDateCF)
    }

    fun hasIgnoreWeekend(ignoreWeekend: Boolean?) = assertAll {
        objects.assertEqual(field("board.ignoreWeekend"), actual.ignoreWeekend, ignoreWeekend)
    }

    fun hasImpedimentColumns(impedimentColumns: List<String>?) = assertAll {
        objects.assertEqual(
            field("board.impedimentColumns"),
            actual.impedimentColumns,
            impedimentColumns?.map { it.toUpperCase() }
        )
    }

    fun hasImpedimentType(impedimentType: ImpedimentType?) = assertAll {
        objects.assertEqual(field("board.impedimentType"), actual.impedimentType, impedimentType)
    }

    fun hasDueDateType(dueDateType: DueDateType?) = assertAll {
        objects.assertEqual(field("board.dueDateType"), actual.dueDateType, dueDateType)
    }

}

fun Board.assertThat(assertions: BoardAssert.() -> Unit): BoardAssert =
    BoardAssert(this).assertThat(assertions)
