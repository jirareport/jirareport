package br.com.jiratorio.domain.jira

import java.time.LocalDateTime

data class JiraChangelog(

    val from: String? = null,

    val to: String? = null,

    val field: String? = null,

    var created: LocalDateTime

)
