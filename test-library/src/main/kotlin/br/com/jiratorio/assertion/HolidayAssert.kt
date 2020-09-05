package br.com.jiratorio.assertion

import br.com.jiratorio.domain.entity.HolidayEntity
import java.time.LocalDate

class HolidayAssert(actual: HolidayEntity) :
    BaseAssert<HolidayAssert, HolidayEntity>(actual, HolidayAssert::class) {

    fun hasDescription(description: String?) = assertAll {
        objects.assertEqual(field("holiday.description"), actual.description, description)
    }

    fun hasDate(date: LocalDate?) = assertAll {
        objects.assertEqual(field("holiday.date"), actual.date, date)
    }

}

fun HolidayEntity.assertThat(assertions: HolidayAssert.() -> Unit): HolidayAssert =
    HolidayAssert(this).assertThat(assertions)
