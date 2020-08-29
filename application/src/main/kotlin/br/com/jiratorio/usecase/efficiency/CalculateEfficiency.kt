package br.com.jiratorio.usecase.efficiency

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Efficiency
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.extension.containsUpperCase
import br.com.jiratorio.extension.time.minutesDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate

@UseCase
class CalculateEfficiency {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        columnChangelog: Set<ColumnChangelog>,
        touchingColumns: MutableList<String>?,
        waitingColumns: MutableList<String>?,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Efficiency {
        log.info(
            "Action=calculateEfficiency, touchingColumns={}, waitingColumns={}, columnChangelog={}, holidays={}, ignoreWeekend={}",
            touchingColumns, waitingColumns, columnChangelog, holidays, ignoreWeekend
        )

        if (touchingColumns == null || touchingColumns.isEmpty()) {
            return Efficiency()
        }

        if (waitingColumns == null || waitingColumns.isEmpty()) {
            return Efficiency()
        }

        val waitTime = calcDurationInColumns(columnChangelog, waitingColumns, holidays, ignoreWeekend)
        val touchTime = calcDurationInColumns(columnChangelog, touchingColumns, holidays, ignoreWeekend)
        val pctEfficiency = calcPctEfficiency(waitTime, touchTime)

        return Efficiency(waitTime, touchTime, pctEfficiency)
    }

    private fun calcPctEfficiency(waitTime: Long, touchTime: Long): Double =
        if (touchTime == 0L && waitTime == 0L)
            0.0
        else
            touchTime.toDouble() / (touchTime + waitTime) * 100

    private fun calcDurationInColumns(
        columnChangelog: Set<ColumnChangelog>,
        columns: List<String>,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Long =
        columnChangelog
            .filter { columns.containsUpperCase(it.to) }
            .map { it.startDate.minutesDiff(it.endDate, holidays, ignoreWeekend) }
            .sum()

}
