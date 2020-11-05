package br.com.jiratorio.domain.issue

import java.time.LocalDateTime

data class MinimalIssue(
    override val id: Long,
    override val key: String,
    override val leadTime: Long,
    override val startDate: LocalDateTime,
    override val endDate: LocalDateTime,
    override val creator: String?,
    override val summary: String,
    override val issueType: String?,
    override val estimate: String?,
    override val project: String?,
    override val epic: String?,
    override val system: String?,
    override val priority: String?,
    override val created: LocalDateTime,
    override val deviationOfEstimate: Long?,
    override val changeEstimateCount: Int,
    override val impedimentTime: Long,
    override val dynamicFields: Map<String, String?>,
) : Issue
