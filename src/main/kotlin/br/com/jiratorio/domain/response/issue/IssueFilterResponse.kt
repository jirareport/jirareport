package br.com.jiratorio.domain.response.issue

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues

data class IssueFilterResponse(
    var estimates: Set<String>,
    var systems: Set<String>,
    var epics: Set<String>,
    var issueTypes: Set<String>,
    var projects: Set<String>,
    var priorities: Set<String>,
    var dynamicFieldsValues: List<DynamicFieldsValues>
)
