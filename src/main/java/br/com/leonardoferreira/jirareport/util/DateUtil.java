package br.com.leonardoferreira.jirareport.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import org.springframework.util.StringUtils;

/**
 * @author lferreira
 * @since 7/31/17 10:30 AM
 */
public final class DateUtil {

    public static final Locale LOCALE_BR = new Locale("pt", "BR");

    private static final String DEFAULT_FORMATTER = "yyyy-MM-dd";
    public static final String FORMATTER = "2018-05-03T23:20:26.000-0300";

    private DateUtil() {
    }

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

    public static Long daysDiff(final LocalDateTime startDate, final LocalDateTime endDate, final List<String> holidays) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        Long workingDays = 0L;
        while (!start.isAfter(end)) {
            DayOfWeek day = start.getDayOfWeek();
            if (!DayOfWeek.SATURDAY.equals(day) && !DayOfWeek.SUNDAY.equals(day) && !isHoliday(start, holidays)) {
                workingDays++;
            }
            start = start.plusDays(1);
        }

        return workingDays;
    }

    private static boolean isHoliday(final LocalDate day, final List<String> holidays) {
        return holidays.contains(day.format(DateTimeFormatter.ofPattern(DEFAULT_FORMATTER)));
    }

    public static LocalDateTime parseFromJira(String date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.parse(date.substring(0, 19), DateTimeFormatter.ISO_DATE_TIME);
    }

}
