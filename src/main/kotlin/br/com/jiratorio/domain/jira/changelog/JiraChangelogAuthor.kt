package br.com.jiratorio.domain.jira.changelog

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraChangelogAuthor(

    val self: String? = null,

    @JsonProperty("name")
    val username: String? = null,

    @JsonProperty("key")
    val key: String? = null,

    @JsonProperty("emailAddress")
    val email: String? = null,

    @JsonProperty("displayName")
    val name: String? = null,

    val active: Boolean? = null,

    val timeZone: String? = null

)
