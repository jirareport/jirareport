package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayApiResponse
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun HolidayApiResponse.toHoliday(board: Board): Holiday =
    Holiday(
        description = name,
        board = board,
        date = LocalDate.parse(date, datePattern)
    )

fun List<HolidayApiResponse>.toHoliday(board: Board): List<Holiday> =
    this.map { it.toHoliday(board) }

fun Holiday.toHolidayResponse(): HolidayResponse =
    HolidayResponse(
        id = id,
        date = date.format(datePattern),
        description = description,
        boardId = board.id
    )

fun Page<Holiday>.toHolidayResponse(): PageImpl<HolidayResponse> =
    PageImpl(
        content.map { it.toHolidayResponse() },
        pageable,
        totalElements
    )

fun HolidayRequest.toHoliday(board: Board): Holiday =
    Holiday(
        description = description,
        board = board,
        date = date
    )

fun Holiday.updateFromHolidayRequest(holidayRequest: HolidayRequest) {
    description = holidayRequest.description
    date = holidayRequest.date
}
