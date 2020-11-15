package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.LeadTimeEntity
import org.assertj.core.api.AbstractAssert
import java.time.LocalDateTime

class LeadTimeAssert(
    actual: LeadTimeEntity?,
) : AbstractAssert<LeadTimeAssert, LeadTimeEntity>(
    actual,
    LeadTimeAssert::class.java
) {

    fun hasLeadTime(leadTime: Long): LeadTimeAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    fun hasStartDate(startDate: LocalDateTime): LeadTimeAssert {
        if (actual.startDate != startDate) {
            failWithMessage(shouldBeEquals(actual.startDate, startDate).create())
        }

        return this
    }

    fun hasEndDate(endDate: LocalDateTime): LeadTimeAssert {
        if (actual.endDate != endDate) {
            failWithMessage(shouldBeEquals(actual.endDate, endDate).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: LeadTimeEntity?): LeadTimeAssert =
            LeadTimeAssert(actual)

    }

}
