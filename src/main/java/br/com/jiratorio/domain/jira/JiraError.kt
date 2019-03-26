package br.com.jiratorio.domain.jira

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraError(
    val message: String,

    @field:JsonProperty("status-code")
    val statusCode: Long,

    var errorMessages: List<String>? = null
)
