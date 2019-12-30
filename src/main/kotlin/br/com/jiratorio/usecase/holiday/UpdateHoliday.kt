package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.mapper.updateFromHolidayRequest
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.HolidayRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateHoliday(
    private val boardRepository: BoardRepository,
    private val holidayRepository: HolidayRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(holidayId: Long, boardId: Long, holidayRequest: HolidayRequest) {
        log.info("Method=execute, holidayId={}, boardId={}, holidayRequest={}", holidayId, boardId, holidayRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

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
