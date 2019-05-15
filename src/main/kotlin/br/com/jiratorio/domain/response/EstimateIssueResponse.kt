package br.com.jiratorio.domain.response

data class EstimateIssueResponse(

    val key: String,

    val summary: String,

    val startDate: String,

    var estimateDateAvg: String,

    var estimateDatePercentile50: String,

    var estimateDatePercentile75: String,

    var estimateDatePercentile90: String,

    val leadTime: Long,

    val created: String,

    val issueType: String? = null,

    val creator: String? = null,

    val estimate: String? = null,

    val system: String? = null,

    val project: String? = null,

    val epic: String? = null,

    val priority: String? = null,

    val changelog: List<ChangelogResponse>,

    val impedimentTime: Long

)
