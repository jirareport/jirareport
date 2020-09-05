package br.com.jiratorio.assertion

import br.com.jiratorio.domain.entity.LeadTimeConfigEntity

class LeadTimeConfigAssert(
    actual: LeadTimeConfigEntity
) : BaseAssert<LeadTimeConfigAssert, LeadTimeConfigEntity>(
    actual,
    LeadTimeConfigAssert::class
) {

    fun hasName(name: String) = assertAll {
        objects.assertEqual(field("leadTimeConfig.name"), actual.name, name)
    }

    fun hasStartColumn(startColumn: String) = assertAll {
        val field = field("leadTimeConfig.startColumn")
        strings.assertUpperCase(field, actual.startColumn)
        objects.assertEqual(field, actual.startColumn, startColumn.toUpperCase())
    }

    fun hasEndColumn(endColumn: String) = assertAll {
        val field = field("leadTimeConfig.endColumn")
        strings.assertUpperCase(field, actual.endColumn)
        objects.assertEqual(field, actual.endColumn, endColumn.toUpperCase())
    }

}

fun LeadTimeConfigEntity.assertThat(assertions: LeadTimeConfigAssert.() -> Unit): LeadTimeConfigAssert =
    LeadTimeConfigAssert(this).assertThat(assertions)
