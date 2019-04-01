package br.com.jiratorio.domain.response.issueperiod

data class IssuePeriodResponse(
    val id: Long,
    val dates: String,
    val wipAvg: Double,
    val leadTime: Double,
    val avgPctEfficiency: Double,
    val jql: String,
    var issuesCount: Int
)
