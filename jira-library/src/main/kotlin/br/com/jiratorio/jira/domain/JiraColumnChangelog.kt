package br.com.jiratorio.jira.domain

import br.com.jiratorio.domain.changelog.ColumnChangelog
import java.time.LocalDateTime

data class JiraColumnChangelog(
    override val from: String? = null,
    override val to: String,
    override val startDate: LocalDateTime,
    override val endDate: LocalDateTime = startDate,
    override val leadTime: Long = 0,
) : ColumnChangelog
