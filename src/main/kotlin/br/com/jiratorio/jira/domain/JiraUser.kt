package br.com.jiratorio.jira.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraUser(

    @field:JsonProperty("displayName")
    val name: String,

    @field:JsonProperty("emailAddress")
    val email: String

)
