package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author lferreira
 * @since 7/31/17 10:30 AM
 */
public final class DateUtil {

    public static final String DEFAULT_FORMATTER = "yyyy-MM-dd";

    public static final Locale LOCALE_BR = new Locale("pt", "BR");

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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

        return new SimpleDateFormat("dd/MM/yyyy", LOCALE_BR).format(c.getTime());
    }

    public static String lastMonthDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE, -1);

        return new SimpleDateFormat("dd/MM/yyyy", LOCALE_BR).format(c.getTime());
    }

    public static String toENDate(final String startDate) {
        if (startDate == null) {
            return null;
        }
        String[] split = startDate.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

    public static int sort(final IssuePeriod issuePeriod, final IssuePeriod issuePeriod1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy", LOCALE_BR);
        try {
            Date d1 = sdf.parse(issuePeriod.getId().getStartDate());
            Date d2 = sdf.parse(issuePeriod1.getId().getStartDate());

            return d1.compareTo(d2);
        } catch (ParseException e) {
            return 0;
        }
    }

    @SneakyThrows
    public static Long daysDiff(final String startDate, final String endDate, final List<String> holidays) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        Calendar start = Calendar.getInstance();
        start.setTime(new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).parse(startDate));
        Calendar end = Calendar.getInstance();
        end.setTime(new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).parse(endDate));
        Long workingDays = 0L;
        while (!start.after(end)) {
            int day = start.get(Calendar.DAY_OF_WEEK);
            if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY) && !isHoliday(start, holidays)) {
                workingDays++;
            }
            start.add(Calendar.DATE, 1);
        }
        return workingDays;
    }

    private static boolean isHoliday(final Calendar day, final List<String> holidays) {
        String aux = new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).format(day.getTime());
        return holidays.contains(aux);
    }

    public static String toENDateFromDisplayDate(final String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        final String[] split = date.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }
}
