package br.com.jiratorio.assertion

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity

class DynamicFieldConfigAssert(
    actual: DynamicFieldConfigEntity
) : BaseAssert<DynamicFieldConfigAssert, DynamicFieldConfigEntity>(
    actual,
    DynamicFieldConfigAssert::class
) {

    fun hasName(name: String) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.name"), actual.name, name)
    }

    fun hasField(field: String) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.field"), actual.field, field)
    }

    fun hasBoard(board: BoardEntity) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.board"), actual.board, board)
    }

}

fun DynamicFieldConfigEntity.assertThat(assertions: DynamicFieldConfigAssert.() -> Unit): DynamicFieldConfigAssert =
    DynamicFieldConfigAssert(this).assertThat(assertions)
