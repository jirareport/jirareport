package br.com.jiratorio.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static final Locale LOCALE_BR = new Locale("pt", "BR");

    public static Long daysDiff(final LocalDateTime startDate, final LocalDateTime endDate,
                                final List<LocalDate> holidays, final Boolean ignoreWeekend) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        return daysDiff(start, end, holidays, ignoreWeekend);
    }

    public static Long daysDiff(final LocalDate startDate, final LocalDate endDate,
                                final List<LocalDate> holidays, final Boolean ignoreWeekend) {
        LocalDate cursor = startDate;

        long workingDays = 0L;
        if (Boolean.TRUE.equals(ignoreWeekend)) {
            workingDays = ChronoUnit.DAYS.between(cursor, endDate) + 1;
        } else {
            while (!cursor.isAfter(endDate)) {
                DayOfWeek day = cursor.getDayOfWeek();
                if (isWorkDay(holidays, day, cursor)) {
                    workingDays++;
                }
                cursor = cursor.plusDays(1);
            }
        }

        return workingDays;
    }

    public static LocalDate addDays(final LocalDateTime startDate, final Long numDays,
                                    final List<LocalDate> holidays, final Boolean ignoreWeekend) {

        if (numDays <= 1) {
            return startDate.toLocalDate();
        }
        if (Boolean.TRUE.equals(ignoreWeekend)) {
            return startDate.toLocalDate().plusDays(numDays - 1);
        }
        Long total = numDays - 1;
        LocalDate ref = startDate.toLocalDate();

        while (total > 0) {
            ref = ref.plusDays(1);
            DayOfWeek day = ref.getDayOfWeek();
            if (isWorkDay(holidays, day, ref)) {
                total--;
            }
        }
        return ref;
    }

    private static boolean isHoliday(final LocalDate day, final List<LocalDate> holidays) {
        return holidays.contains(day);
    }

    public static LocalDateTime parseFromJira(final String date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.parse(date.substring(0, 19), DateTimeFormatter.ISO_DATE_TIME);
    }

    private static boolean isWorkDay(final List<LocalDate> holidays, final DayOfWeek day, final LocalDate localDate) {
        return !DayOfWeek.SATURDAY.equals(day) && !DayOfWeek.SUNDAY.equals(day) && !isHoliday(localDate, holidays);
    }

}
