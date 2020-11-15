package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.entity.BoardEntity
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase
import org.assertj.core.error.ShouldContain.shouldContain

class BoardAssert(
    actual: BoardEntity,
) : AbstractAssert<BoardAssert, BoardEntity>(
    actual,
    BoardAssert::class.java
) {

    fun hasName(name: String?): BoardAssert {
        if (actual.name != name) {
            failWithMessage(shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasExternalId(externalId: Long?): BoardAssert {
        if (actual.externalId != externalId) {
            failWithMessage(shouldBeEquals(actual.externalId, externalId).create())
        }

        return this
    }

    fun hasOwner(owner: String): BoardAssert {
        if (actual.owner != owner) {
            failWithMessage(shouldBeEquals(actual.owner, owner).create())
        }

        return this
    }

    fun hasStartColumn(startColumn: String?): BoardAssert {
        if (actual.startColumn != actual.startColumn?.toUpperCase()) {
            failWithMessage(shouldBeUpperCase(actual.startColumn).create())
        }

        if (actual.startColumn != startColumn?.toUpperCase()) {
            failWithMessage(shouldBeEquals(actual.startColumn, startColumn?.toUpperCase()).create())
        }

        return this
    }

    fun hasEndColumn(endColumn: String?): BoardAssert {
        if (actual.endColumn != actual.endColumn?.toUpperCase()) {
            failWithMessage(shouldBeUpperCase(actual.endColumn).create())
        }

        if (actual.endColumn != endColumn?.toUpperCase()) {
            failWithMessage(shouldBeEquals(actual.endColumn, endColumn?.toUpperCase()).create())
        }

        return this
    }

    fun hasFluxColumn(fluxColumn: List<String>?): BoardAssert {
        if (actual.fluxColumn != fluxColumn) {
            failWithMessage(shouldBeEquals(actual.fluxColumn, fluxColumn).create())
        }

        return this
    }

    fun hasTouchingColumns(touchingColumns: List<String>?): BoardAssert {
        val notFound = containsUppercase(actual.touchingColumns ?: emptyList(), touchingColumns ?: emptyList())

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.touchingColumns, touchingColumns, notFound).create())
        }

        return this
    }

    fun hasWaitingColumns(waitingColumns: List<String>?): BoardAssert {
        val notFound = containsUppercase(actual.waitingColumns ?: emptyList(), waitingColumns ?: emptyList())

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.waitingColumns, waitingColumns, notFound).create())
        }

        return this
    }

    fun hasIgnoreIssueType(ignoreIssueType: List<String>?): BoardAssert {
        val notFound = ignoreIssueType?.filter { actual.ignoreIssueType?.contains(it) != true }

        if (!notFound.isNullOrEmpty()) {
            failWithMessage(shouldContain(actual.ignoreIssueType, ignoreIssueType, notFound).create())
        }

        return this
    }

    fun hasEpicCF(epicCF: String?): BoardAssert {
        if (actual.epicCF != epicCF) {
            failWithMessage(shouldBeEquals(actual.epicCF, epicCF).create())
        }

        return this
    }

    fun hasEstimateCF(estimateCF: String?): BoardAssert {
        if (actual.estimateCF != estimateCF) {
            failWithMessage(shouldBeEquals(actual.estimateCF, estimateCF).create())
        }

        return this
    }

    fun hasSystemCF(systemCF: String?): BoardAssert {
        if (actual.systemCF != systemCF) {
            failWithMessage(shouldBeEquals(actual.systemCF, systemCF).create())
        }

        return this
    }

    fun hasProjectCF(projectCF: String?): BoardAssert {
        if (actual.projectCF != projectCF) {
            failWithMessage(shouldBeEquals(actual.projectCF, projectCF).create())
        }

        return this
    }

    fun hasDueDateCF(dueDateCF: String?): BoardAssert {
        if (actual.dueDateCF != dueDateCF) {
            failWithMessage(shouldBeEquals(actual.dueDateCF, dueDateCF).create())
        }

        return this
    }

    fun hasIgnoreWeekend(ignoreWeekend: Boolean?): BoardAssert {
        if (actual.ignoreWeekend != ignoreWeekend) {
            failWithMessage(shouldBeEquals(actual.ignoreWeekend, ignoreWeekend).create())
        }

        return this
    }

    fun hasImpedimentColumns(impedimentColumns: List<String>?): BoardAssert {
        if (actual.impedimentColumns != impedimentColumns) {
            failWithMessage(shouldBeEquals(actual.impedimentColumns, impedimentColumns).create())
        }

        return this
    }

    fun hasImpedimentType(impedimentType: ImpedimentType?): BoardAssert {
        if (actual.impedimentType != impedimentType) {
            failWithMessage(shouldBeEquals(actual.impedimentType, impedimentType).create())
        }

        return this
    }

    fun hasDueDateType(dueDateType: DueDateType?): BoardAssert {
        if (actual.dueDateType != dueDateType) {
            failWithMessage(shouldBeEquals(actual.dueDateType, dueDateType).create())
        }

        return this
    }

    private fun containsUppercase(actual: List<String>, touchingColumns: List<String>) =
        touchingColumns.map { it.toUpperCase() }.filter { !actual.contains(it) }

    companion object {

        fun assertThat(actual: BoardEntity): BoardAssert =
            BoardAssert(actual)

    }

}
