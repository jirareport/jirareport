package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

/**
 * @author lferreira
 * @since 7/31/17 10:30 AM
 */
public final class DateUtil {

    public static final Locale LOCALE_BR = new Locale("pt", "BR");

    private static final String DEFAULT_FORMATTER = "yyyy-MM-dd";

    private DateUtil() {
    }

    public static String displayFormat(final String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        String[] split = date.split("-");
        return split[2] + "/" + split[1] + "/" + split[0];
    }

    public static String firstMonthDay() {
        return LocalDate.now()
                .withDayOfMonth(1)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String lastMonthDay() {
        return LocalDate.now()
                .plus(1, ChronoUnit.MONTHS)
                .withDayOfMonth(1)
                .minus(1, ChronoUnit.DAYS)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String toENDate(final String startDate) {
        if (startDate == null) {
            return null;
        }
        String[] split = startDate.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

    public static int sort(final IssuePeriod issuePeriod, final IssuePeriod issuePeriod1) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(issuePeriod.getId().getStartDate(), formatter)
                .compareTo(LocalDate.parse(issuePeriod1.getId().getStartDate(), formatter));
    }

    @SneakyThrows
    public static Long daysDiff(final String startDate, final String endDate, final List<String> holidays) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DEFAULT_FORMATTER));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DEFAULT_FORMATTER));

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

    public static String toENDateFromDisplayDate(final String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        final String[] split = date.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

}
