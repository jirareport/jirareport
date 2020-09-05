package br.com.jiratorio.domain

import java.time.LocalDateTime

data class MinimalIssue(
    val id: Long,
    val key: String,
    val leadTime: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val creator: String?,
    val summary: String,
    val issueType: String?,
    val estimate: String?,
    val project: String?,
    val epic: String?,
    val system: String?,
    val priority: String?,
    val created: LocalDateTime,
    val deviationOfEstimate: Long?,
    val changeEstimateCount: Int,
    val impedimentTime: Long,
    val dynamicFields: Map<String, String?>
)
