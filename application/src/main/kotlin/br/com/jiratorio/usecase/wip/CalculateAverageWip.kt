package br.com.jiratorio.usecase.wip

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.time.rangeTo
import org.slf4j.LoggerFactory
import java.time.LocalDate

@UseCase
class CalculateAverageWip {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(start: LocalDate, end: LocalDate, issues: List<Issue>, wipColumns: Set<String>): Double {
        log.info("Action=calculateAverageWip, start={}, end={}, issues={}, wipColumns={}", start, end, issues, wipColumns)

        return (start..end)
            .map { time ->
                issues.count { issue ->
                    issue.wasOnAnyWipColumnAtThisTime(wipColumns, time)
                }
            }
            .average()
            .zeroIfNaN()
    }

    private fun Issue.wasOnAnyWipColumnAtThisTime(wipColumns: Set<String>, time: LocalDate): Boolean =
        columnChangelog.any { time in it.dateRange && it.to.toUpperCase() in wipColumns }

}
