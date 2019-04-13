package br.com.jiratorio.domain.jira.changelog

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class JiraChangelogHistory(

    val id: String,

    val author: JiraChangelogAuthor,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val created: LocalDateTime,

    val items: List<JiraChangelogItem>

)
