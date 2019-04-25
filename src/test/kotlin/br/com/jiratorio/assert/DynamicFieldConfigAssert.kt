package br.com.jiratorio.assert

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig

class DynamicFieldConfigAssert(actual: DynamicFieldConfig) :
    BaseAssert<DynamicFieldConfigAssert, DynamicFieldConfig>(actual, DynamicFieldConfigAssert::class) {

    fun hasName(name: String) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.name"), actual.name, name)
    }

    fun hasField(field: String) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.field"), actual.field, field)
    }

    fun hasBoard(board: Board) = assertAll {
        objects.assertEqual(field("dynamicFieldConfig.board"), actual.board, board)
    }

}
