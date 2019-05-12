package br.com.jiratorio.domain.response.issue

data class IssueResponse(
    val id: Long,
    val key: String,
    val creator: String?,
    val summary: String,
    val issueType: String?,
    val estimate: String?,
    val project: String?,
    val epic: String?,
    val system: String?,
    val priority: String?,
    val leadTime: Long,
    val startDate: String,
    val endDate: String,
    val created: String,
    val deviationOfEstimate: Long?,
    val changeEstimateCount: Int?,
    val impedimentTime: Long,
    val dynamicFields: Map<String, String?>?,
    val detailsUrl: String
)
