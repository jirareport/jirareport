package br.com.jiratorio.domain.issue

import br.com.jiratorio.domain.changelog.Changelog
import java.time.LocalDateTime

data class JiraIssue(
    val key: String,
    val issueType: String?,
    val leadTime: Long,
    val creator: String?,
    val created: LocalDateTime,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val system: String?,
    val epic: String?,
    val estimate: String?,
    val project: String?,
    val summary: String,
    val changelog: Changelog,
    val priority: String?,
    val dynamicFields: Map<String, String?>,
)
