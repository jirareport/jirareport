package br.com.jiratorio.domain.response.issue

import br.com.jiratorio.domain.DynamicFieldsValues

data class IssueFilterResponse(

    val estimates: Set<String>,

    val systems: Set<String>,

    val epics: Set<String>,

    val issueTypes: Set<String>,

    val projects: Set<String>,

    val priorities: Set<String>,

    val dynamicFieldsValues: List<DynamicFieldsValues>

)
