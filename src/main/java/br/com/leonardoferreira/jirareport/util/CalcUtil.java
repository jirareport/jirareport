package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.vo.Percentile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalcUtil {

    public static Set<String> calcStartColumns(final String startColumn,
                                        final String endColumn,
                                        final List<String> fluxColumn) {
        Set<String> startColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (startColumn == null) {
            return startColumns;
        }

        startColumns.add(startColumn);
        if (fluxColumn != null && !fluxColumn.isEmpty() && endColumn != null) {
            int start = fluxColumn.indexOf(startColumn);
            int end = fluxColumn.indexOf(endColumn);
            if (start >= 0 && end >= 0 && start < end) {
                startColumns.addAll(fluxColumn.subList(start + 1, end + 1));
            }
        }
        return startColumns;
    }

    public static Set<String> calcWipColumns(final String startColumn,
                                               final String endColumn,
                                               final List<String> fluxColumn) {
        Set<String> wipColumns = calcStartColumns(startColumn, endColumn, fluxColumn);
        wipColumns.remove(endColumn);
        return wipColumns;
    }

    public static Set<String> calcEndColumns(final String endColumn,
                                             final List<String> fluxColumn) {
        Set<String> endColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (endColumn == null) {
            return endColumns;
        }
        endColumns.add(endColumn);
        if (fluxColumn != null && !fluxColumn.isEmpty()) {
            int start = fluxColumn.indexOf(endColumn);
            if (start >= 0 && start < fluxColumn.size() - 1) {
                endColumns.addAll(fluxColumn.subList(start + 1, fluxColumn.size()));
            }
        }
        return endColumns;
    }

    public static Set<String> calcStartColumns(final Board board) {
        return CalcUtil.calcStartColumns(board.getStartColumn(), board.getEndColumn(), board.getFluxColumn());
    }

    public static Set<String> calcEndColumns(final Board board) {
        return CalcUtil.calcEndColumns(board.getEndColumn(), board.getFluxColumn());
    }

    public static Set<String> calcWipColumns(final Board board) {
        return CalcUtil.calcWipColumns(board.getStartColumn(), board.getEndColumn(), board.getFluxColumn());
    }

    public static int calculateCeilingPercentage(final int totalElements, final int percentage) {
        return new BigDecimal((double) totalElements * percentage / 100).setScale(0, RoundingMode.CEILING)
                .intValue();
    }

    public static Percentile calculatePercentile(final List<Long> list) {
        Long median = 0L;
        Long percentile75 = 0L;
        Long percentile90 = 0L;

        double average = list.parallelStream()
                .filter(Objects::nonNull)
                .mapToLong(i -> i)
                .average().orElse(0D);

        list.sort(Comparator.naturalOrder());
        if (!list.isEmpty()) {
            int medianIndex = calculateCeilingPercentage(list.size(), 50);
            int percentile75Index = calculateCeilingPercentage(list.size(), 75);
            int percentile90Index = calculateCeilingPercentage(list.size(), 90);

            median = list.get(medianIndex - 1);
            percentile75 = list.get(percentile75Index - 1);
            percentile90 = list.get(percentile90Index - 1);
        }

        return Percentile.builder()
                .average(average)
                .median(median)
                .percentile75(percentile75)
                .percentile90(percentile90)
                .build();
    }
}
