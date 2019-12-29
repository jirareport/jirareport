package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.usecase.holiday.CreateHoliday
import br.com.jiratorio.usecase.holiday.DeleteHoliday
import br.com.jiratorio.usecase.holiday.FindHolidayByBoard
import br.com.jiratorio.usecase.holiday.FindHoliday
import br.com.jiratorio.usecase.holiday.FindHolidayDaysByBoard
import br.com.jiratorio.usecase.holiday.ImportHolidays
import br.com.jiratorio.usecase.holiday.UpdateHoliday
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class HolidayServiceImpl(
    private val createHoliday: CreateHoliday,
    private val deleteHoliday: DeleteHoliday,
    private val findHolidayByBoard: FindHolidayByBoard,
    private val findHolidayById: FindHoliday,
    private val findHolidayDaysByBoard: FindHolidayDaysByBoard,
    private val importHolidays: ImportHolidays,
    private val updateHoliday: UpdateHoliday
) : HolidayService {

    @Transactional(readOnly = true)
    override fun findByBoard(boardId: Long, pageable: Pageable): Page<HolidayResponse> =
        findHolidayByBoard.execute(boardId, pageable)

    @Transactional(readOnly = true)
    override fun findDaysByBoard(boardId: Long): List<LocalDate> =
        findHolidayDaysByBoard.execute(boardId)

    @Transactional
    override fun create(boardId: Long, holidayRequest: HolidayRequest): Long =
        createHoliday.execute(boardId, holidayRequest)

    @Transactional
    override fun delete(id: Long) =
        deleteHoliday.execute(id)

    @Transactional(readOnly = true)
    override fun findById(id: Long): HolidayResponse =
        findHolidayById.execute(id)

    @Transactional
    override fun update(boardId: Long, holidayId: Long, holidayRequest: HolidayRequest) =
        updateHoliday.execute(boardId, holidayId, holidayRequest)

    @Transactional
    override fun importHolidays(boardId: Long, currentUser: Account) =
        importHolidays.execute(boardId, currentUser)

}
