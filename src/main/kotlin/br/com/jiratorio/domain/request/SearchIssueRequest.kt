package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import java.time.LocalDate

data class SearchIssueRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val keys: MutableList<String> = mutableListOf(),
    val systems: MutableList<String> = mutableListOf(),
    val estimates: MutableList<String> = mutableListOf(),
    val epics: MutableList<String> = mutableListOf(),
    val issueTypes: MutableList<String> = mutableListOf(),
    val projects: MutableList<String> = mutableListOf(),
    val priorities: MutableList<String> = mutableListOf(),
    val dynamicFieldsValues: MutableList<DynamicFieldsValues> = mutableListOf()
)
