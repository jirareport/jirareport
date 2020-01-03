package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.exception.MissingBoardConfigurationException
import br.com.jiratorio.extension.containsUpperCase
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
        get() = if (orderedColumns.isNullOrEmpty())
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

    fun calcStartAndEndDate(columnChangelog: Set<ColumnChangelog>, created: LocalDateTime): Pair<LocalDateTime?, LocalDateTime?> {
        var startDate: LocalDateTime? = null
        var endDate: LocalDateTime? = null

        for (cl in columnChangelog) {
            if (startDate == null && startColumns.containsUpperCase(cl.to)) {
                startDate = lastOccurrenceIfNeeds(cl.to, columnChangelog, cl.startDate)
            }

            if (endDate == null && endColumns.containsUpperCase(cl.to)) {
                endDate = lastOccurrenceIfNeeds(cl.to, columnChangelog, cl.startDate)
            }
        }

        if (startLeadTimeColumn == "BACKLOG") {
            startDate = lastOccurrenceIfNeeds("BACKLOG", columnChangelog, created)
        }

        return startDate to endDate
    }

    private fun lastOccurrenceIfNeeds(
        to: String?,
        columnChangelog: Set<ColumnChangelog>,
        created: LocalDateTime
    ): LocalDateTime =
        if (useLastOccurrenceWhenCalculateLeadTime)
            columnChangelog.last {
                it.to.equals(to, ignoreCase = true)
            }.startDate
        else
            created

}
