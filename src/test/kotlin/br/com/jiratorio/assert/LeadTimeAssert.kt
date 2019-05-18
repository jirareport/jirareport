package br.com.jiratorio.assert

import br.com.jiratorio.domain.entity.LeadTime
import java.time.LocalDateTime

class LeadTimeAssert(actual: LeadTime) : BaseAssert<LeadTimeAssert, LeadTime>(actual, LeadTimeAssert::class) {

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
