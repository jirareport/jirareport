package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import lombok.AllArgsConstructor
import lombok.ToString

@ToString
@AllArgsConstructor
data class FluxColumn(
    val startLeadTimeColumn: String,
    val endLeadTimeColumn: String,
    val orderedColumns: List<String>?
) {

    constructor(board: Board) : this(board.startColumn!!, board.endColumn!!, board.fluxColumn)

    val endColumns: Set<String>
        get() = if (orderedColumns == null)
            setOf(endLeadTimeColumn)
        else
            orderedColumns.takeLastWhile { it != endLeadTimeColumn }.toSet() + endLeadTimeColumn

    val startColumns: Set<String>
        get() = if (orderedColumns == null)
            setOf(startLeadTimeColumn)
        else with(orderedColumns) {
            takeWhile { it != endLeadTimeColumn } - takeWhile { it != startLeadTimeColumn } + endLeadTimeColumn
        }.toSet()

    val wipColumns: Set<String?>
        get() = startColumns - endLeadTimeColumn

    val lastColumn: String
        get() = orderedColumns?.last() ?: "Done"

}
