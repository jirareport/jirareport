package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.IssueResult;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author leferreira
 * @since 7/31/17 10:30 AM
 */
public class DateUtil {

    public static final String DEFAULT_FORMATTER = "yyyy-MM-dd";

    @SneakyThrows
    public static Long daysDiff(String startDate, String endDate) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        Calendar start = Calendar.getInstance();
        start.setTime(new SimpleDateFormat(DEFAULT_FORMATTER).parse(startDate));
        Calendar end = Calendar.getInstance();
        end.setTime(new SimpleDateFormat(DEFAULT_FORMATTER).parse(endDate));
        Long workingDays = 0L;
        while (!start.after(end)) {
            int day = start.get(Calendar.DAY_OF_WEEK);
            if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY) && !isHoliday(start)) {
                workingDays++;
            }
            start.add(Calendar.DATE, 1);
        }
        return workingDays;
    }

    private static boolean isHoliday(Calendar day) {
        String aux = new SimpleDateFormat(DEFAULT_FORMATTER).format(day.getTime());
        return Arrays.asList("2017-06-15").contains(aux);
    }

    public static String displayFormat(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        String[] split = date.split("-");
        return split[2] + "/" + split[1] + "/" + split[0];
    }

    public static String firstMonthDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

        return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
    }

    public static String lastMonthDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE, -1);

        return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
    }

    public static String toENDate(final String startDate) {
        String[] split = startDate.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

    public static int sort(final IssueResult issueResult, final IssueResult issueResult1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
        try {
            Date d1 = sdf.parse(issueResult.getForm().getStartDate());
            Date d2 = sdf.parse(issueResult1.getForm().getStartDate());

            return d1.compareTo(d2);
        } catch (ParseException e) {
            return 0;
        }
    }
}
