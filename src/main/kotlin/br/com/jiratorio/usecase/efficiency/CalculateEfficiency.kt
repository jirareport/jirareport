package br.com.jiratorio.usecase.efficiency

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Efficiency
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.containsUpperCase
import br.com.jiratorio.extension.time.minutesDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate

@UseCase
class CalculateEfficiency {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        changelog: List<Changelog>,
        touchingColumns: MutableList<String>?,
        waitingColumns: MutableList<String>?,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Efficiency {
        log.info(
            "Action=calculateEfficiency, touchingColumns={}, waitingColumns={}, changelog={}, holidays={}, ignoreWeekend={}",
            touchingColumns, waitingColumns, changelog, holidays, ignoreWeekend
        )

        if (touchingColumns == null || touchingColumns.isEmpty()) {
            return Efficiency()
        }

        if (waitingColumns == null || waitingColumns.isEmpty()) {
            return Efficiency()
        }

        val waitTime = calcDurationInColumns(changelog, waitingColumns, holidays, ignoreWeekend)
        val touchTime = calcDurationInColumns(changelog, touchingColumns, holidays, ignoreWeekend)
        val pctEfficiency = calcPctEfficiency(waitTime, touchTime)

        return Efficiency(waitTime, touchTime, pctEfficiency)
    }

    private fun calcPctEfficiency(waitTime: Long, touchTime: Long): Double {
        return if (touchTime == 0L && waitTime == 0L) {
            0.0
        } else {
            touchTime.toDouble() / (touchTime + waitTime) * 100
        }
    }

    private fun calcDurationInColumns(
        changelog: List<Changelog>,
        columns: List<String>,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Long {
        return changelog
            .filter { columns.containsUpperCase(it.to) }
            .map { it.created.minutesDiff(it.endDate, holidays, ignoreWeekend) }
            .sum()
    }

}
