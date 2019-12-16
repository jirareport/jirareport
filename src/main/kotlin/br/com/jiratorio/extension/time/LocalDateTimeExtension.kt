package br.com.jiratorio.extension.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDateTime?.minutesDiff(endDate: LocalDateTime?, holidays: List<LocalDate>, ignoreWeekend: Boolean?): Long {
    if (this == null || endDate == null) {
        return 0
    }

    var start: LocalDateTime = this

    var workingHours = 0L
    if (ignoreWeekend == true) {
        workingHours = ChronoUnit.MINUTES.between(this, endDate) + 1
    } else {
        while (!start.isAfter(endDate)) {
            if (start.toLocalDate().isWorkday(holidays)) {
                val endOfDay = start.toLocalDate().atTime(LocalTime.MAX)

                workingHours += if (endOfDay.isBefore(endDate)) {
                    ChronoUnit.MINUTES.between(start, endOfDay) + 1
                } else {
                    ChronoUnit.MINUTES.between(start, endDate) + 1
                }
            }

            start = start.plusDays(1).truncatedTo(ChronoUnit.DAYS)
        }
    }

    return workingHours
}

fun LocalDateTime?.daysDiff(endDate: LocalDateTime?, holidays: List<LocalDate>, ignoreWeekend: Boolean?): Long =
    this?.toLocalDate().daysDiff(endDate?.toLocalDate(), holidays, ignoreWeekend)

fun LocalDateTime.plusDays(days: Long, holidays: List<LocalDate>, ignoreWeekend: Boolean?): LocalDate {
    val current = this.toLocalDate()

    if (days <= 1) {
        return current
    }

    if (ignoreWeekend == true) {
        return current.plusDays(days - 1)
    }

    var totalDaysToAdd: Long = days - 1
    var cursor = current

    while (totalDaysToAdd > 0) {
        cursor = cursor.plusDays(1)
        if (cursor.isWorkday(holidays)) {
            totalDaysToAdd--
        }
    }

    return cursor
}

private val dateTimePattern: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun LocalDateTime.displayFormat(): String =
    this.format(dateTimePattern)
