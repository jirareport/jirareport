package br.com.jiratorio.domain.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.AssertTrue

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

    val priorities: MutableList<String> = mutableListOf()

) {

    @AssertTrue(message = "{validations.start-date-cant-be-before-end-date}")
    fun isStartDateIsBeforeEndDate(): Boolean =
        startDate.isBefore(endDate) || startDate.isEqual(endDate)

}
