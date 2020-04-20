package br.com.jiratorio.domain.issueperiodnameformat

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal object InitialAndFinalDateFormatter : IssuePeriodNameFormatter {

    private val FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale): String =
        "[${startDate.format(FORMATTER)} - ${endDate.format(FORMATTER)}]"

}
