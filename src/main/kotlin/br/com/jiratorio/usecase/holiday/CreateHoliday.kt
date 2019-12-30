package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.mapper.toHoliday
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.HolidayRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateHoliday(
    private val boardRepository: BoardRepository,
    private val holidayRepository: HolidayRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long, holidayRequest: HolidayRequest): Long {
        log.info("Action=createHoliday, boardId={}, holidayRequest={}", boardId, holidayRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        val existentHoliday = holidayRepository.findByDateAndBoardId(holidayRequest.date, boardId)
        if (existentHoliday != null) {
            throw UniquenessFieldException("date")
        }

        val holiday = holidayRequest.toHoliday(board)
        holidayRepository.save(holiday)

        return holiday.id
    }
}
