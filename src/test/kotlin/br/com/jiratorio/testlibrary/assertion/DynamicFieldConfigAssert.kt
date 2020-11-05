package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import org.assertj.core.api.AbstractAssert

class DynamicFieldConfigAssert(
    actual: DynamicFieldConfigEntity,
) : AbstractAssert<DynamicFieldConfigAssert, DynamicFieldConfigEntity>(
    actual,
    DynamicFieldConfigAssert::class.java
) {

    fun hasName(name: String): DynamicFieldConfigAssert {
        if (actual.name != name) {
            failWithMessage(shouldBeEquals(actual.name, name).create())
        }

        return this
    }

    fun hasField(field: String): DynamicFieldConfigAssert {
        if (actual.field != field) {
            failWithMessage(shouldBeEquals(actual.field, field).create())
        }

        return this
    }

    fun hasBoard(board: BoardEntity): DynamicFieldConfigAssert {
        if (actual.board != board) {
            failWithMessage(shouldBeEquals(actual.board, board).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: DynamicFieldConfigEntity): DynamicFieldConfigAssert =
            DynamicFieldConfigAssert(actual)

    }

}
