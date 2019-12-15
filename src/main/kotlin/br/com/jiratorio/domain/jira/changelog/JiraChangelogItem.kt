package br.com.jiratorio.domain.jira.changelog

import br.com.jiratorio.extension.toStringBuilder
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

data class JiraChangelogItem(

    val field: String? = null,

    val fieldtype: String? = null,

    val from: String? = null,

    val fromString: String? = null,

    val to: String? = null,

    val toString: String? = null,

    @JsonIgnore
    var created: LocalDateTime? = null

) {

    override fun toString(): String =
        toStringBuilder(
            JiraChangelogItem::field,
            JiraChangelogItem::fieldtype
        )

}
