package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toHoliday
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.usecase.board.FindBoard
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateHoliday(
    private val findBoardById: FindBoard,
    private val holidayRepository: HolidayRepository
) {

    @Transactional
    fun execute(boardId: Long, holidayRequest: HolidayRequest): Long {
        log.info("Method=execute, boardId={}, holidayRequest={}", boardId, holidayRequest)

        val board = findBoardById.execute(boardId)

        val existentHoliday = holidayRepository.findByDateAndBoardId(holidayRequest.date, boardId)
        if (existentHoliday != null) {
            throw UniquenessFieldException("date")
        }

        val holiday = holidayRequest.toHoliday(board)
        holidayRepository.save(holiday)

        return holiday.id
    }
}
