package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.Board;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author lferreira
 * @since 5/18/18 6:43 PM
 */
public final class CalcUtil {

    private CalcUtil() {
    }

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

}
