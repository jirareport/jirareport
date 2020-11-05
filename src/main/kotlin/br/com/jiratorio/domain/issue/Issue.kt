package br.com.jiratorio.domain.issue

import java.time.LocalDateTime

interface Issue {
    val id: Long
    val key: String
    val leadTime: Long
    val startDate: LocalDateTime
    val endDate: LocalDateTime
    val creator: String?
    val summary: String
    val issueType: String?
    val estimate: String?
    val project: String?
    val epic: String?
    val system: String?
    val priority: String?
    val created: LocalDateTime
    val deviationOfEstimate: Long?
    val changeEstimateCount: Int
    val impedimentTime: Long
    val dynamicFields: Map<String, String?>
}
