package br.com.jiratorio.domain.issueperiodnameformat

import java.time.LocalDate
import java.util.Locale

internal interface IssuePeriodNameFormatter {

    fun format(startDate: LocalDate, endDate: LocalDate, locale: Locale = Locale.ENGLISH): String

}
