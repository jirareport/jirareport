package br.com.jiratorio.usecase.chart.issue

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.IssueProgression
import br.com.jiratorio.extension.time.daysBetween
import br.com.jiratorio.extension.time.displayFormat
import br.com.jiratorio.extension.time.isBetween
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

@UseCase
class CreateIssueProgressionChart {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(board: Board, issues: List<Issue>): IssueProgression {
        log.info("Action=createIssueProgressionChart, board={}, issues={}", board, issues)

        val start = issues
            .minBy(Issue::startDate)
            ?.startDate
            ?.toLocalDate()

        val end = issues
            .maxBy(Issue::endDate)
            ?.endDate
            ?.toLocalDate()

        val days = start.daysBetween(
            endDate = end,
            holidays = board.holidays?.map(Holiday::date) ?: emptyList(),
            ignoreWeekend = board.ignoreWeekend
        )

        val result = issues.asSequence()
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
