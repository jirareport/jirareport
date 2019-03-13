package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import java.util.TreeSet
import lombok.AllArgsConstructor
import lombok.ToString
import org.springframework.util.CollectionUtils

@ToString
@AllArgsConstructor
data class FluxColumn(
        val startLeadTimeColumn: String?,
        val endLeadTimeColumn: String?,
        val orderedColumns: List<String>?
) {

    constructor(board: Board) : this(board.startColumn, board.endColumn, board.fluxColumn) {}

    val startColumns: MutableSet<String>
        get() {
            val startColumns = TreeSet(String.CASE_INSENSITIVE_ORDER)
            if (startLeadTimeColumn == null) {
                return startColumns
            }

            startColumns.add(startLeadTimeColumn)
            if (!CollectionUtils.isEmpty(orderedColumns)) {
                val start = orderedColumns!!.indexOf(startLeadTimeColumn)
                val end = orderedColumns.indexOf(endLeadTimeColumn)
                if (start >= 0 && end >= 0 && start < end) {
                    startColumns.addAll(orderedColumns.subList(start + 1, end + 1))
                }
            }
            return startColumns
        }

    val endColumns: Set<String>
        get() {
            val endColumns = TreeSet(String.CASE_INSENSITIVE_ORDER)
            if (endLeadTimeColumn == null) {
                return endColumns
            }
            endColumns.add(endLeadTimeColumn)
            if (!CollectionUtils.isEmpty(orderedColumns)) {
                val start = orderedColumns!!.indexOf(endLeadTimeColumn)
                if (start >= 0 && start < orderedColumns.size - 1) {
                    endColumns.addAll(orderedColumns.subList(start + 1, orderedColumns.size))
                }
            }
            return endColumns
        }

    val wipColumns: Set<String>
        get() {
            val wipColumns = startColumns
            wipColumns.remove(endLeadTimeColumn)
            return wipColumns
        }

    val lastColumn: String
        get() = if (CollectionUtils.isEmpty(orderedColumns)) "Done" else orderedColumns!![orderedColumns.size - 1]

}
