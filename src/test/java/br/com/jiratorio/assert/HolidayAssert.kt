package br.com.jiratorio.assert

import br.com.jiratorio.domain.entity.Holiday
import java.time.LocalDate

class HolidayAssert(actual: Holiday) :
        BaseAssert<HolidayAssert, Holiday>(actual, HolidayAssert::class) {

    fun hasDescription(description: String) = assertAll {
        objects.assertEqual(field("holiday.description"), actual.description, description)
    }

    fun hasDate(date: LocalDate) = assertAll {
        objects.assertEqual(field("holiday.date"), actual.date, date)
    }

}
