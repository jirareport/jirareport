package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.HolidayApiResponse
import br.com.jiratorio.domain.response.HolidayResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

@Component
class HolidayMapper {

    fun fromApiResponse(holidayApiResponse: HolidayApiResponse, board: Board): Holiday {
        return Holiday(
            description = holidayApiResponse.name,
            board = board,
            date = LocalDate.parse(holidayApiResponse.date, ofPattern("dd/MM/yyyy"))
        )
    }

    fun fromApiResponse(holidayApiResponses: List<HolidayApiResponse>, board: Board): List<Holiday> {
        return holidayApiResponses.map { fromApiResponse(it, board) }
    }

    fun toHolidayResponse(holiday: Holiday): HolidayResponse {
        return HolidayResponse(
            id = holiday.id,
            date = holiday.date.format(ofPattern("dd/MM/yyyy")),
            description = holiday.description,
            boardId = holiday.board.id
        )
    }

    fun toHolidayResponse(holidays: List<Holiday>): List<HolidayResponse> {
        return holidays.map { toHolidayResponse(it) }
    }

    fun toHolidayResponse(holidays: Page<Holiday>): PageImpl<HolidayResponse> {
        return PageImpl(
            toHolidayResponse(holidays.content),
            holidays.pageable,
            holidays.totalElements
        )
    }

    fun toHoliday(holidayRequest: HolidayRequest, board: Board): Holiday {
        return Holiday(
            description = holidayRequest.description,
            board = board,
            date = holidayRequest.date
        )
    }

    fun updateFromRequest(holiday: Holiday, holidayRequest: HolidayRequest): Holiday {
        return holiday.apply {
            description = holidayRequest.description
            date = holidayRequest.date
        }
    }

}
