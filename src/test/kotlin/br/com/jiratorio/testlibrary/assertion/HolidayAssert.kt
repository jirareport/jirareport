package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.HolidayEntity
import org.assertj.core.api.AbstractAssert
import java.time.LocalDate

class HolidayAssert(
    actual: HolidayEntity,
) : AbstractAssert<HolidayAssert, HolidayEntity>(
    actual,
    HolidayAssert::class.java
) {

    fun hasDescription(description: String?): HolidayAssert {
        if (actual.description != description) {
            failWithMessage(shouldBeEquals(actual.description, description).create())
        }

        return this
    }

    fun hasDate(date: LocalDate?): HolidayAssert {
        if (actual.date != date) {
            failWithMessage(shouldBeEquals(actual.date, date).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: HolidayEntity): HolidayAssert =
            HolidayAssert(actual)

    }

}
