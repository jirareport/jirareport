package br.com.jiratorio.strategy.issueperiodnameformat

import br.com.jiratorio.domain.IssuePeriodNameFormat
import br.com.jiratorio.extension.time.isFirstDayOfMonth
import br.com.jiratorio.extension.time.isLastDayOfMonth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal interface IssuePeriodNameFormatter {

    fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale = Locale.ENGLISH): String

    companion object {

        fun from(issuePeriodNameFormat: IssuePeriodNameFormat): IssuePeriodNameFormatter =
            when (issuePeriodNameFormat) {
                IssuePeriodNameFormat.INITIAL_AND_FINAL_DATE -> InitialAndFinal
                IssuePeriodNameFormat.MONTH -> Month
                IssuePeriodNameFormat.MONTH_AND_YEAR -> MonthAndYear
                IssuePeriodNameFormat.MONTH_AND_ABBREVIATED_YEAR -> MonthAndAbbreviatedYear
                IssuePeriodNameFormat.ABBREVIATED_MONTH -> AbbreviatedMonth
                IssuePeriodNameFormat.ABBREVIATED_MONTH_AND_YEAR -> AbbreviatedMonthAndYear
                IssuePeriodNameFormat.ABBREVIATED_MONTH_AND_ABBREVIATED_YEAR -> AbbreviatedMonthAndAbbreviatedYear
            }

    }

}

private object InitialAndFinal : IssuePeriodNameFormatter {

    private val FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale): String =
        "[${startDate.format(FORMATTER)} - ${endDate.format(FORMATTER)}]"

}

private object Month : PatternBasedFormatter(pattern = "MMMM")

private object MonthAndYear : PatternBasedFormatter(pattern = "MMMM/yyyy")

private object MonthAndAbbreviatedYear : PatternBasedFormatter(pattern = "MMMM/yy")

private object AbbreviatedMonth : PatternBasedFormatter(pattern = "MMM")

private object AbbreviatedMonthAndYear : PatternBasedFormatter(pattern = "MMM/yyyy")

private object AbbreviatedMonthAndAbbreviatedYear : PatternBasedFormatter(pattern = "MMM/yy")

private open class PatternBasedFormatter(
    private val pattern: String,
) : IssuePeriodNameFormatter {

    override fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale): String =
        if (startDate.month == endDate.month && startDate.isFirstDayOfMonth && endDate.isLastDayOfMonth)
            locale.formatter.format(startDate).capitalize()
        else
            InitialAndFinal.format(startDate, endDate, locale)

    private val Locale.formatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(pattern, this)

}
