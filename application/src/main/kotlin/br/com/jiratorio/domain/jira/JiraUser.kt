package br.com.jiratorio.domain.jira

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraUser(

    @JsonProperty("displayName")
    val name: String,

    @JsonProperty("emailAddress")
    val email: String

)
