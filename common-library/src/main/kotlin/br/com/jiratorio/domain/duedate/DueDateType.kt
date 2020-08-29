package br.com.jiratorio.domain.duedate

import br.com.jiratorio.domain.duedate.calculator.DueDateCalculator
import br.com.jiratorio.domain.duedate.calculator.FirstAndLastDueDateCalculator
import br.com.jiratorio.domain.duedate.calculator.FirstDueDateAndEndDateCalculator
import br.com.jiratorio.domain.duedate.calculator.LastDueDateAndEndDateCalculator

enum class DueDateType(calculator: DueDateCalculator) : DueDateCalculator by calculator {
    FIRST_AND_LAST_DUE_DATE(FirstAndLastDueDateCalculator),
    FIRST_DUE_DATE_AND_END_DATE(FirstDueDateAndEndDateCalculator),
    LAST_DUE_DATE_AND_END_DATE(LastDueDateAndEndDateCalculator)
}
