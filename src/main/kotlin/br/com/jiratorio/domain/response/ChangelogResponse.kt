package br.com.jiratorio.domain.response

data class ChangelogResponse(
    var from: String?,
    var to: String?,
    var startDate: String,
    var endDate: String,
    var leadTime: Long
)
