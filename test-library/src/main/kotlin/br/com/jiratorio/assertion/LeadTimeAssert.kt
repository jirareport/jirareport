package br.com.jiratorio.assertion

import br.com.jiratorio.domain.entity.LeadTimeEntity
import java.time.LocalDateTime

class LeadTimeAssert(
    actual: LeadTimeEntity
) : BaseAssert<LeadTimeAssert, LeadTimeEntity>(
    actual,
    LeadTimeAssert::class
) {

    fun hasLeadTime(leadTime: Long) = assertAll {
        objects.assertEqual(field("leadTime.leadTime"), actual.leadTime, leadTime)
    }

    fun hasStartDate(startDate: LocalDateTime) = assertAll {
        objects.assertEqual(field("leadTime.startDate"), actual.startDate, startDate)
    }

    fun hasEndDate(endDate: LocalDateTime) = assertAll {
        objects.assertEqual(field("leadTime.endDate"), actual.endDate, endDate)
    }

}

fun LeadTimeEntity.assertThat(assertions: LeadTimeAssert.() -> Unit): LeadTimeAssert =
    LeadTimeAssert(this).assertThat(assertions)
