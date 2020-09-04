package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayApiResponse
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.extension.time.displayFormat
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun HolidayApiResponse.toHoliday(board: Board): Holiday =
    Holiday(
        description = name,
        board = board,
        date = date
    )

fun List<HolidayApiResponse>.toHoliday(board: Board): List<Holiday> =
    this.map { it.toHoliday(board) }

fun Holiday.toHolidayResponse(): HolidayResponse =
    HolidayResponse(
        id = id,
        date = date.displayFormat(),
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
