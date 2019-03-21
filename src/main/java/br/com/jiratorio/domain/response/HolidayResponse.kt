package br.com.jiratorio.domain.response

data class HolidayResponse(
    var id: Long,
    var date: String,
    var description: String,
    var boardId: Long
)
