package br.com.jiratorio.domain

data class IssuePeriodDetails(
    var avgLeadTime: Double? = null,
    var issueCount: Int? = null,
    var boardId: Long? = null,
    var jql: String? = null,
    var wipAvg: Double? = null,
    var avgPctEfficiency: Double? = null
)
