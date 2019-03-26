package br.com.jiratorio.domain.jira

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraUser(

    @JsonProperty("displayName")
    var name: String,

    @JsonProperty("emailAddress")
    var email: String

)
