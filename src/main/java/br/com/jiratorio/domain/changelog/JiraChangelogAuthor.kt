package br.com.jiratorio.domain.changelog

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraChangelogAuthor(
    var self: String? = null,

    @JsonProperty("name")
    var username: String? = null,

    @JsonProperty("key")
    var key: String? = null,

    @JsonProperty("emailAddress")
    var email: String? = null,

    @JsonProperty("displayName")
    var name: String? = null,

    var active: Boolean? = null,

    var timeZone: String? = null
)
