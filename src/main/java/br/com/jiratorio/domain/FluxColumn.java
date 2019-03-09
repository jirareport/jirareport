package br.com.jiratorio.domain;

import br.com.jiratorio.domain.entity.Board;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

@ToString
@AllArgsConstructor
public class FluxColumn {

    private String startLeadTimeColumn;

    private String endLeadTimeColumn;

    private List<String> orderedColumns;

    public FluxColumn(final Board board) {
        this(board.getStartColumn(), board.getEndColumn(), board.getFluxColumn());
    }

    public Set<String> getStartColumns() {
        Set<String> startColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (startLeadTimeColumn == null) {
            return startColumns;
        }

        startColumns.add(startLeadTimeColumn);
        if (!CollectionUtils.isEmpty(orderedColumns)) {
            int start = orderedColumns.indexOf(startLeadTimeColumn);
            int end = orderedColumns.indexOf(endLeadTimeColumn);
            if (start >= 0 && end >= 0 && start < end) {
                startColumns.addAll(orderedColumns.subList(start + 1, end + 1));
            }
        }
        return startColumns;
    }

    public Set<String> getEndColumns() {
        Set<String> endColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (endLeadTimeColumn == null) {
            return endColumns;
        }
        endColumns.add(endLeadTimeColumn);
        if (!CollectionUtils.isEmpty(orderedColumns)) {
            int start = orderedColumns.indexOf(endLeadTimeColumn);
            if (start >= 0 && start < orderedColumns.size() - 1) {
                endColumns.addAll(orderedColumns.subList(start + 1, orderedColumns.size()));
            }
        }
        return endColumns;
    }

    public Set<String> getWipColumns() {
        Set<String> wipColumns = getStartColumns();
        wipColumns.remove(endLeadTimeColumn);
        return wipColumns;
    }

    public String getLastColumn() {
        return CollectionUtils.isEmpty(orderedColumns) ? "Done" : orderedColumns.get(orderedColumns.size() - 1);
    }
}
