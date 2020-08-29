package br.com.jiratorio.domain.response.holiday

data class HolidayResponse(

    val id: Long,

    val date: String,

    val description: String,

    val boardId: Long

)
