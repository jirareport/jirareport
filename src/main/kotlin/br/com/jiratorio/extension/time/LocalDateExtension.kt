package br.com.jiratorio.extension.time

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDate.isHoliday(holidays: List<LocalDate>): Boolean =
    this in holidays

fun LocalDate.isWorkday(holidays: List<LocalDate>): Boolean =
    DayOfWeek.SATURDAY != dayOfWeek &&
        DayOfWeek.SUNDAY != dayOfWeek &&
        !this.isHoliday(holidays)

fun LocalDate?.daysDiff(endDate: LocalDate?, holidays: List<LocalDate>, ignoreWeekend: Boolean?): Long {
    if (this == null || endDate == null) {
        return 0L
    }

    return if (ignoreWeekend == true) {
        ChronoUnit.DAYS.between(this, endDate) + 1
    } else {
        (this..endDate).count {
            it.isWorkday(holidays)
        }.toLong()
    }
}

fun LocalDate.atEndOfDay(): LocalDateTime =
    this.atTime(LocalTime.MAX)

private val datePattern: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun LocalDate.displayFormat(): String =
    this.format(datePattern)
