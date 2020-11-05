package br.com.jiratorio.domain.response

data class ChangelogResponse(

    val from: String?,

    val to: String?,

    val startDate: String,

    val endDate: String,

    val leadTime: Long

)
