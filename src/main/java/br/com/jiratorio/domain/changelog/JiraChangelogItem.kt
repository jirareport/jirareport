package br.com.jiratorio.domain.changelog

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

data class JiraChangelogItem(
    var field: String? = null,

    var fieldtype: String? = null,

    var from: String? = null,

    var fromString: String? = null,

    var to: String? = null,

    var toString: String? = null,

    @JsonIgnore
    var created: LocalDateTime? = null
)
