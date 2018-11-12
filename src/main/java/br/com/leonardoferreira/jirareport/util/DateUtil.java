package br.com.leonardoferreira.jirareport.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static final Locale LOCALE_BR = new Locale("pt", "BR");

    private static final String DEFAULT_FORMATTER = "yyyy-MM-dd";

    public static String displayFormat(final String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        String[] split = date.split("-");
        return split[2] + "/" + split[1] + "/" + split[0];
    }

    public static LocalDate firstMonthDay() {
        return LocalDate.now()
                .withDayOfMonth(1);
    }

    public static LocalDate lastMonthDay() {
        return LocalDate.now()
                .plus(1, ChronoUnit.MONTHS)
                .withDayOfMonth(1)
                .minus(1, ChronoUnit.DAYS);
    }

    public static String toENDate(final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static int sort(final IssuePeriod issuePeriod, final IssuePeriod issuePeriod1) {
        return issuePeriod.getStartDate().compareTo(issuePeriod1.getStartDate());
    }

    public static Long daysDiff(final LocalDateTime startDate, final LocalDateTime endDate,
                                final List<LocalDate> holidays, final Boolean ignoreWeekend) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        long workingDays = 0L;
        if (Boolean.TRUE.equals(ignoreWeekend)) {
            workingDays = ChronoUnit.DAYS.between(start, end) + 1;
        } else {
            while (!start.isAfter(end)) {
                DayOfWeek day = start.getDayOfWeek();
                if (!DayOfWeek.SATURDAY.equals(day) && !DayOfWeek.SUNDAY.equals(day) && !isHoliday(start, holidays)) {
                    workingDays++;
                }
                start = start.plusDays(1);
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
            if (!DayOfWeek.SATURDAY.equals(day) && !DayOfWeek.SUNDAY.equals(day) && !isHoliday(ref, holidays)) {
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

}
