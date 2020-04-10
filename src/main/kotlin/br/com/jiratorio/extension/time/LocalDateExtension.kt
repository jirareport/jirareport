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
            !isHoliday(holidays)

fun LocalDate?.daysDiff(endDate: LocalDate?, holidays: List<LocalDate>, ignoreWeekend: Boolean?): Long {
    if (this == null || endDate == null) {
        return 0L
    }

    return if (ignoreWeekend == true) {
        ChronoUnit.DAYS.between(this, endDate) + 1
    } else {
        (this..endDate)
            .count { it.isWorkday(holidays) }
            .toLong()
    }
}

fun LocalDate.atEndOfDay(): LocalDateTime =
    this.atTime(LocalTime.MAX)

fun LocalDate.displayFormat(
    datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
): String =
    this.format(datePattern)

fun LocalDate.isBetween(startDate: LocalDate, endDate: LocalDate): Boolean =
    this == startDate || this == endDate || isAfter(startDate) && isBefore(endDate)

fun LocalDate?.daysBetween(
    endDate: LocalDate?,
    holidays: List<LocalDate>,
    ignoreWeekend: Boolean?
): List<LocalDate> =
    if (this == null || endDate == null)
        emptyList()
    else if (ignoreWeekend == true)
        (this..endDate).toList()
    else
        (this..endDate).filter { it.isWorkday(holidays) }
