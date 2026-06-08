package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase

class LeadTimeConfigAssert(
    actual: LeadTimeConfigEntity,
) : AbstractAssert<LeadTimeConfigAssert, LeadTimeConfigEntity>(
    actual,
    LeadTimeConfigAssert::class.java
) {

    fun hasName(name: String): LeadTimeConfigAssert {
        if (actual.name != name) {
            failWithMessage(shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasStartColumn(startColumn: String): LeadTimeConfigAssert {
        if (actual.startColumn != actual.startColumn.uppercase()) {
            failWithMessage(shouldBeUpperCase(actual.startColumn).create())
        }

        if (actual.startColumn != startColumn.uppercase()) {
            failWithMessage(shouldBeEquals(actual.startColumn, startColumn.uppercase()).create())
        }

        return this
    }

    fun hasEndColumn(endColumn: String): LeadTimeConfigAssert {
        if (actual.endColumn != actual.endColumn.uppercase()) {
            failWithMessage(shouldBeUpperCase(actual.endColumn).create())
        }

        if (actual.endColumn != endColumn.uppercase()) {
            failWithMessage(shouldBeEquals(actual.endColumn, endColumn.uppercase()).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: LeadTimeConfigEntity): LeadTimeConfigAssert =
            LeadTimeConfigAssert(actual)

    }

}
