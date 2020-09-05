package br.com.jiratorio.usecase.chart.issue

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.domain.entity.embedded.IssueProgression
import br.com.jiratorio.extension.time.daysBetween
import br.com.jiratorio.extension.time.displayFormat
import br.com.jiratorio.extension.time.isBetween
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

@UseCase
class CreateIssueProgressionChart {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(board: BoardEntity, issues: List<MinimalIssue>): IssueProgression {
        log.info("Action=createIssueProgressionChart, board={}, issues={}", board, issues)

        if (issues.isEmpty()) {
            return IssueProgression()
        }

        val sortedIssues = issues.sortedBy(MinimalIssue::startDate)

        val start = sortedIssues.first()
            .startDate
            .toLocalDate()

        val end = issues
            .maxByOrNull(MinimalIssue::endDate)
            ?.endDate
            ?.toLocalDate()

        val days = start.daysBetween(
            endDate = end,
            holidays = board.holidays?.map(HolidayEntity::date) ?: emptyList(),
            ignoreWeekend = board.ignoreWeekend
        )

        val result = sortedIssues.asSequence()
            .map { Triple(it.key, it.startDate.toLocalDate(), it.endDate.toLocalDate()) }
            .map { (key, startDate, endDate) ->

                val daysInProgress = days.map { day ->
                    day.isBetween(startDate, endDate)
                }

                Pair(key, daysInProgress)
            }
            .toMap()

        return IssueProgression(
            days = days.map { it.displayFormat(DateTimeFormatter.ofPattern("dd/MM")) },
            issues = result
        )
    }

}
