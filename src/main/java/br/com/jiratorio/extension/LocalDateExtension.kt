package br.com.jiratorio.extension

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.isHoliday(holidays: List<LocalDate>?): Boolean {
    return holidays?.contains(this) == true
}

fun LocalDate.isWorkDay(holidays: List<LocalDate>?): Boolean {
    return DayOfWeek.SATURDAY != this.dayOfWeek &&
            DayOfWeek.SUNDAY != this.dayOfWeek &&
            !this.isHoliday(holidays)
}
