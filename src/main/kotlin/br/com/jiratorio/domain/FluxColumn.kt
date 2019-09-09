package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.exception.MissingBoardConfigurationException
import java.time.LocalDateTime

data class FluxColumn(
    val startLeadTimeColumn: String,
    val endLeadTimeColumn: String,
    val orderedColumns: List<String>?,
    val useLastOccurrenceWhenCalculateLeadTime: Boolean = false
) {

    constructor(board: Board) : this(
        startLeadTimeColumn = board.startColumn ?: throw MissingBoardConfigurationException("startColumn"),
        endLeadTimeColumn = board.endColumn ?: throw MissingBoardConfigurationException("endColumn"),
        orderedColumns = board.fluxColumn,
        useLastOccurrenceWhenCalculateLeadTime = board.useLastOccurrenceWhenCalculateLeadTime
    )

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
        get() = if (orderedColumns.isNullOrEmpty())
            "DONE"
        else
            orderedColumns.last()

    fun calcStartAndEndDate(changelog: List<Changelog>, created: LocalDateTime): Pair<LocalDateTime?, LocalDateTime?> {
        var startDate: LocalDateTime? = null
        var endDate: LocalDateTime? = null

        for (cl in changelog) {
            if (startDate == null && startColumns.contains(cl.to?.toUpperCase())) {
                startDate = if (useLastOccurrenceWhenCalculateLeadTime)
                    changelog.last { it.to == cl.to }.created
                else
                    cl.created
            }

            if (endDate == null && endColumns.contains(cl.to?.toUpperCase())) {
                endDate = if (useLastOccurrenceWhenCalculateLeadTime)
                    changelog.last { it.to == cl.to }.created
                else
                    cl.created
            }
        }

        if (startLeadTimeColumn == "BACKLOG") {
            startDate = created
        }

        return startDate to endDate
    }

}
