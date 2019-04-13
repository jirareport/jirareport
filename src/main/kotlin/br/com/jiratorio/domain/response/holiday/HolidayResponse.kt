package br.com.jiratorio.domain.response.holiday

data class HolidayResponse(
    var id: Long,
    var date: String,
    var description: String,
    var boardId: Long
)
