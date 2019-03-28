package br.com.jiratorio.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun LocalDateTime?.minutesDiff(endDate: LocalDateTime?, holidays: List<LocalDate>?, ignoreWeekend: Boolean?): Long {
    if (this == null || endDate == null) {
        return 0
    }

    var start: LocalDateTime = this

    var workingHours = 0L
    if (ignoreWeekend == true) {
        workingHours = ChronoUnit.MINUTES.between(this, endDate) + 1
    } else {
        while (!start.isAfter(endDate)) {
            if (start.toLocalDate().isWorkDay(holidays)) {
                val endOfDay = start.toLocalDate().atTime(LocalTime.MAX)

                if (endOfDay.isBefore(endDate)) {
                    workingHours += ChronoUnit.MINUTES.between(start, endOfDay) + 1
                } else {
                    workingHours += ChronoUnit.MINUTES.between(start, endDate) + 1
                }
            }

            start = start.plusDays(1).truncatedTo(ChronoUnit.DAYS)
        }
    }

    return workingHours
}
