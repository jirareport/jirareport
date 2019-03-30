package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDateTime

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
        get() = orderedColumns?.run {
            takeWhile { it != endLeadTimeColumn } - takeWhile { it != startLeadTimeColumn } + endLeadTimeColumn
        }?.toSet() ?: setOf(startLeadTimeColumn)

    val wipColumns: Set<String>
        get() = startColumns - endLeadTimeColumn

    val lastColumn: String
        get() = orderedColumns?.last() ?: "Done"

    fun calcStartAndEndDate(changelog: List<Changelog>, created: LocalDateTime): Pair<LocalDateTime?, LocalDateTime?> {
        val startColumns = startColumns
        val endColumns = endColumns

        var startDate: LocalDateTime? = null
        var endDate: LocalDateTime? = null

        for (cl in changelog) {
            if (startDate == null && startColumns.contains(cl.to)) {
                startDate = cl.created
            }

            if (endDate == null && endColumns.contains(cl.to)) {
                endDate = cl.created
            }
        }

        if (startLeadTimeColumn == "BACKLOG") {
            startDate = created
        }

        return startDate to endDate
    }

}
