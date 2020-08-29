package br.com.jiratorio.domain.issueperiodnameformat

import br.com.jiratorio.extension.time.isFirstDayOfMonth
import br.com.jiratorio.extension.time.isLastDayOfMonth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class PatternBasedFormatter(
    private val pattern: String
) : IssuePeriodNameFormatter {

    override fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale): String =
        if (startDate.month == endDate.month && startDate.isFirstDayOfMonth && endDate.isLastDayOfMonth)
            locale.formatter.format(startDate).capitalize()
        else
            InitialAndFinalDateFormatter.format(startDate, endDate, locale)

    private val Locale.formatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(pattern, this)

}
