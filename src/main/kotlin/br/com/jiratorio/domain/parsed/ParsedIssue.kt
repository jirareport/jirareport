package br.com.jiratorio.domain.parsed

import java.time.LocalDateTime

data class ParsedIssue(
    val key: String,
    val issueType: String?,
    val creator: String?,
    val created: LocalDateTime,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val system: String?,
    val epic: String?,
    val estimate: String?,
    val project: String?,
    val summary: String,
    val parsedChangelog: ParsedChangelog,
    val priority: String?,
    val dynamicFields: Map<String, String?>?
)
