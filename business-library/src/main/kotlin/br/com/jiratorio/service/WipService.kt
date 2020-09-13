package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.extension.decimal.zeroIfNaN
import br.com.jiratorio.extension.time.rangeTo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WipService {

    private val log = LoggerFactory.getLogger(javaClass)

    fun calculateAverage(start: LocalDate, end: LocalDate, issues: List<IssueEntity>, wipColumns: Set<String>): Double {
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

    private fun IssueEntity.wasOnAnyWipColumnAtThisTime(wipColumns: Set<String>, time: LocalDate): Boolean =
        columnChangelog.any { time in it.dateRange && it.to.toUpperCase() in wipColumns }

}
