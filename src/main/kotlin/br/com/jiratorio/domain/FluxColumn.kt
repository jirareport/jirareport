package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.embedded.Changelog
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

    fun calcStartAndEndDate(changelog: List<Changelog>, created: LocalDateTime): Pair<LocalDateTime?, LocalDateTime?> {
        var startDate: LocalDateTime? = null
        var endDate: LocalDateTime? = null

        for (cl in changelog) {
            if (startDate == null && startColumns.containsUpperCase(cl.to)) {
                startDate = lastOccurrenceIfNeeds(cl.to, changelog, cl.created)
            }

            if (endDate == null && endColumns.containsUpperCase(cl.to)) {
                endDate = lastOccurrenceIfNeeds(cl.to, changelog, cl.created)
            }
        }

        if (startLeadTimeColumn == "BACKLOG") {
            startDate = lastOccurrenceIfNeeds("BACKLOG", changelog, created)
        }

        return startDate to endDate
    }

    private fun lastOccurrenceIfNeeds(
        to: String?,
        changelog: List<Changelog>,
        created: LocalDateTime
    ): LocalDateTime =
        if (useLastOccurrenceWhenCalculateLeadTime)
            changelog.last {
                it.to.equals(to, ignoreCase = true)
            }.created
        else
            created

}
