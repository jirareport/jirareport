package br.com.jiratorio.domain.response.issueperiod

data class IssuePeriodResponse(

    val id: Long,

    val name: String,

    val wipAvg: Double,

    val leadTime: Double,

    val avgPctEfficiency: Double,

    val jql: String,

    val throughput: Int,

    val detailsUrl: String

)
