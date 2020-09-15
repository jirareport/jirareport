package br.com.jiratorio.assertion.response.board

import br.com.jiratorio.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import org.assertj.core.api.AbstractAssert

class BoardDetailsResponseAssert private constructor(
    actual: BoardDetailsResponse,
) : AbstractAssert<BoardDetailsResponseAssert, BoardDetailsResponse>(
    actual, BoardDetailsResponseAssert::class.java
) {

    fun hasId(id: Long): BoardDetailsResponseAssert {
        if (actual.id != id) {
            failWithMessage(shouldBeEquals(actual.id, id).create())
        }

        return this
    }

    fun hasExternalId(externalId: Long): BoardDetailsResponseAssert {
        if (actual.externalId != externalId) {
            failWithMessage(shouldBeEquals(actual.externalId, externalId).create())
        }

        return this
    }

    fun hasName(name: String): BoardDetailsResponseAssert {
        if (actual.name != name) {
            failWithMessage(shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasStartColumn(startColumn: String?): BoardDetailsResponseAssert {
        if (actual.startColumn != startColumn) {
            failWithMessage(shouldBeEquals(actual.startColumn, startColumn).create())
        }

        return this
    }

    fun hasEndColumn(endColumn: String?): BoardDetailsResponseAssert {
        if (actual.endColumn != endColumn) {
            failWithMessage(shouldBeEquals(actual.endColumn, endColumn).create())
        }

        return this
    }

    fun hasFluxColumn(fluxColumn: List<String>?): BoardDetailsResponseAssert {
        if (actual.fluxColumn != fluxColumn) {
            failWithMessage(shouldBeEquals(actual.fluxColumn, fluxColumn).create())
        }

        return this
    }

    fun hasIgnoreIssueType(ignoreIssueType: List<String>?): BoardDetailsResponseAssert {
        if (actual.ignoreIssueType != ignoreIssueType) {
            failWithMessage(shouldBeEquals(actual.ignoreIssueType, ignoreIssueType).create())
        }

        return this
    }

    fun hasEpicCF(epicCF: String?): BoardDetailsResponseAssert {
        if (actual.epicCF != epicCF) {
            failWithMessage(shouldBeEquals(actual.epicCF, epicCF).create())
        }

        return this
    }

    fun hasEstimateCF(estimateCF: String?): BoardDetailsResponseAssert {
        if (actual.estimateCF != estimateCF) {
            failWithMessage(shouldBeEquals(actual.estimateCF, estimateCF).create())
        }

        return this
    }

    fun hasSystemCF(systemCF: String?): BoardDetailsResponseAssert {
        if (actual.systemCF != systemCF) {
            failWithMessage(shouldBeEquals(actual.systemCF, systemCF).create())
        }

        return this
    }

    fun hasProjectCF(projectCF: String?): BoardDetailsResponseAssert {
        if (actual.projectCF != projectCF) {
            failWithMessage(shouldBeEquals(actual.projectCF, projectCF).create())
        }

        return this
    }

    fun hasIgnoreWeekend(ignoreWeekend: Boolean?): BoardDetailsResponseAssert {
        if (actual.ignoreWeekend != ignoreWeekend) {
            failWithMessage(shouldBeEquals(actual.ignoreWeekend, ignoreWeekend).create())
        }

        return this
    }

    fun hasDueDateCF(dueDateCF: String?): BoardDetailsResponseAssert {
        if (actual.dueDateCF != dueDateCF) {
            failWithMessage(shouldBeEquals(actual.dueDateCF, dueDateCF).create())
        }

        return this
    }

    fun hasDueDateType(dueDateType: String?): BoardDetailsResponseAssert {
        if (actual.dueDateType != dueDateType) {
            failWithMessage(shouldBeEquals(actual.dueDateType, dueDateType).create())
        }

        return this
    }

    fun hasImpedimentType(impedimentType: ImpedimentType?): BoardDetailsResponseAssert {
        if (actual.impedimentType != impedimentType) {
            failWithMessage(shouldBeEquals(actual.impedimentType, impedimentType).create())
        }

        return this
    }

    fun hasImpedimentColumns(impedimentColumns: List<String>?): BoardDetailsResponseAssert {
        if (actual.impedimentColumns != impedimentColumns) {
            failWithMessage(shouldBeEquals(actual.impedimentColumns, impedimentColumns).create())
        }

        return this
    }

    fun hasTouchingColumns(touchingColumns: List<String>?): BoardDetailsResponseAssert {
        if (actual.touchingColumns != touchingColumns) {
            failWithMessage(shouldBeEquals(actual.touchingColumns, touchingColumns).create())
        }

        return this
    }

    fun hasWaitingColumns(waitingColumns: List<String>?): BoardDetailsResponseAssert {
        if (actual.waitingColumns != waitingColumns) {
            failWithMessage(shouldBeEquals(actual.waitingColumns, waitingColumns).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: BoardDetailsResponse) =
            BoardDetailsResponseAssert(actual)

    }

}
