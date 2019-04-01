package br.com.jiratorio.service

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import java.time.LocalDate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HolidayService {

    fun findByBoard(boardId: Long, pageable: Pageable): Page<HolidayResponse>

    fun findDaysByBoard(boardId: Long): List<LocalDate>

    fun create(boardId: Long, holidayRequest: HolidayRequest): Long

    fun delete(id: Long)

    fun findById(id: Long): HolidayResponse

    fun update(boardId: Long, holidayId: Long, holidayRequest: HolidayRequest)

    fun createImported(boardId: Long, currentUser: Account)

}
