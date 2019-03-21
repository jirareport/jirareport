package br.com.jiratorio.domain

import com.fasterxml.jackson.annotation.JsonProperty

class CurrentUser(

    @JsonProperty("displayName")
    var name: String? = null,

    @JsonProperty("emailAddress")
    var email: String? = null

)
