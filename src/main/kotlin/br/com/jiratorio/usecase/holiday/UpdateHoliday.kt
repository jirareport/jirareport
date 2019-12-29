package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.updateFromHolidayRequest
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.usecase.board.FindBoard
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateHoliday(
    private val findBoardById: FindBoard,
    private val holidayRepository: HolidayRepository
) {

    @Transactional
    fun execute(boardId: Long, holidayId: Long, holidayRequest: HolidayRequest) {
        log.info("Method=execute, boardId={}, holidayId={}, holidayRequest={}", boardId, holidayId, holidayRequest)

        val board = findBoardById.execute(boardId)

        val holiday = holidayRepository.findByIdAndBoard(holidayId, board)
            ?: throw ResourceNotFound()

        val existentHoliday = holidayRepository.findByDateAndBoardId(holidayRequest.date, boardId)
        if (existentHoliday != null && existentHoliday.id != holidayId) {
            throw UniquenessFieldException("date")
        }

        holiday.updateFromHolidayRequest(holidayRequest)

        holidayRepository.save(holiday)
    }
}
