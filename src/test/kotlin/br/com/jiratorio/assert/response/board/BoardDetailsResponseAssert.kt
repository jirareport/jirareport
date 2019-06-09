package br.com.jiratorio.assert.response.board

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.impediment.ImpedimentType
import br.com.jiratorio.domain.response.board.BoardDetailsResponse

class BoardDetailsResponseAssert(
    actual: BoardDetailsResponse
) : BaseAssert<BoardDetailsResponseAssert, BoardDetailsResponse>(
    actual,
    BoardDetailsResponseAssert::class
) {

    fun hasId(id: Long) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.id"), actual.id, id)
    }

    fun hasExternalId(externalId: Long) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.externalId"), actual.externalId, externalId)
    }

    fun hasName(name: String) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.name"), actual.name, name)
    }

    fun hasStartColumn(startColumn: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.startColumn"), actual.startColumn, startColumn)
    }

    fun hasEndColumn(endColumn: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.endColumn"), actual.endColumn, endColumn)
    }

    fun hasFluxColumn(fluxColumn: List<String>?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.fluxColumn"), actual.fluxColumn, fluxColumn)
    }

    fun hasIgnoreIssueType(ignoreIssueType: List<String>?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.ignoreIssueType"), actual.ignoreIssueType, ignoreIssueType)
    }

    fun hasEpicCF(epicCF: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.epicCF"), actual.epicCF, epicCF)
    }

    fun hasEstimateCF(estimateCF: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.estimateCF"), actual.estimateCF, estimateCF)
    }

    fun hasSystemCF(systemCF: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.systemCF"), actual.systemCF, systemCF)
    }

    fun hasProjectCF(projectCF: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.projectCF"), actual.projectCF, projectCF)
    }

    fun hasIgnoreWeekend(ignoreWeekend: Boolean?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.ignoreWeekend"), actual.ignoreWeekend, ignoreWeekend)
    }

    fun hasDueDateCF(dueDateCF: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.dueDateCF"), actual.dueDateCF, dueDateCF)
    }

    fun hasDueDateType(dueDateType: String?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.dueDateType"), actual.dueDateType, dueDateType)
    }

    fun hasImpedimentType(impedimentType: ImpedimentType?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.impedimentType"), actual.impedimentType, impedimentType)
    }

    fun hasImpedimentColumns(impedimentColumns: List<String>?) = assertAll {
        objects.assertEqual(
            field("boardDetailsResponse.impedimentColumns"),
            actual.impedimentColumns,
            impedimentColumns
        )
    }

    fun hasTouchingColumns(touchingColumns: List<String>?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.touchingColumns"), actual.touchingColumns, touchingColumns)
    }

    fun hasWaitingColumns(waitingColumns: List<String>?) = assertAll {
        objects.assertEqual(field("boardDetailsResponse.waitingColumns"), actual.waitingColumns, waitingColumns)
    }

}

fun BoardDetailsResponse.assertThat(assertions: BoardDetailsResponseAssert.() -> Unit): BoardDetailsResponseAssert =
    BoardDetailsResponseAssert(this).assertThat(assertions)
