package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayApiResponse
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.extension.time.displayFormat
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun HolidayApiResponse.toHoliday(board: BoardEntity): HolidayEntity =
    HolidayEntity(
        description = name,
        board = board,
        date = date
    )

fun List<HolidayApiResponse>.toHoliday(board: BoardEntity): List<HolidayEntity> =
    this.map { it.toHoliday(board) }

fun HolidayEntity.toHolidayResponse(): HolidayResponse =
    HolidayResponse(
        id = id,
        date = date.displayFormat(),
        description = description,
        boardId = board.id
    )

fun Page<HolidayEntity>.toHolidayResponse(): PageImpl<HolidayResponse> =
    PageImpl(
        content.map { it.toHolidayResponse() },
        pageable,
        totalElements
    )

fun HolidayRequest.toHoliday(board: BoardEntity): HolidayEntity =
    HolidayEntity(
        description = description,
        board = board,
        date = date
    )

fun HolidayEntity.updateFromHolidayRequest(holidayRequest: HolidayRequest) {
    description = holidayRequest.description
    date = holidayRequest.date
}
