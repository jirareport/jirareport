package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class SearchIssueRequest(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
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
