package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.exception.HolidaysAlreadyImported
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toHoliday
import br.com.jiratorio.mapper.toHolidayResponse
import br.com.jiratorio.mapper.updateFromHolidayRequest
import br.com.jiratorio.provider.HolidayProvider
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.service.board.BoardService
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class HolidayService(
    private val messageResolver: MessageResolver,
    private val boardService: BoardService,
    private val userConfigService: UserConfigService,
    private val holidayProvider: HolidayProvider,
    private val holidayRepository: HolidayRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(boardId: Long, holidayRequest: HolidayRequest): Long {
        log.info("boardId={}, holidayRequest={}", boardId, holidayRequest)

        val board = boardService.findById(boardId)

        val existentHoliday = holidayRepository.findByDateAndBoardId(holidayRequest.date, boardId)
        if (existentHoliday != null) {
            throw UniquenessFieldException("date")
        }

        val holiday = holidayRequest.toHoliday(board)
        holidayRepository.save(holiday)

        return holiday.id
    }

    @Transactional
    fun delete(boardId: Long, holidayId: Long) {
        log.info("boardId={}, holidayId={}", boardId, holidayId)

        val board = boardService.findById(boardId)
        val holiday = holidayRepository.findByBoardAndId(board, holidayId)
            ?: throw ResourceNotFound()

        holidayRepository.delete(holiday)
    }

    @Transactional(readOnly = true)
    fun findAll(boardId: Long, pageable: Pageable): Page<HolidayResponse> {
        log.info("boardId={}", boardId)

        return holidayRepository
            .findAllByBoardId(boardId, pageable)
            .toHolidayResponse()
    }

    @Transactional(readOnly = true)
    fun findAllDaysByBoard(boardId: Long): List<LocalDate> {
        log.info("boardId={}", boardId)

        return holidayRepository
            .findAllByBoardId(boardId)
            .map { it.date }
    }

    @Transactional(readOnly = true)
    fun findById(boardId: Long, holidayId: Long): HolidayResponse {
        log.info("boardId={}, holidayId={}", boardId, holidayId)

        val board = boardService.findById(boardId)
        val holiday = holidayRepository.findByBoardAndId(board, holidayId)
            ?: throw ResourceNotFound()

        return holiday.toHolidayResponse()
    }

    @Transactional
    fun update(boardId: Long, holidayId: Long, holidayRequest: HolidayRequest) {
        log.info("holidayId={}, boardId={}, holidayRequest={}", holidayId, boardId, holidayRequest)

        val board = boardService.findById(boardId)
        val holiday = holidayRepository.findByBoardAndId(board, holidayId)
            ?: throw ResourceNotFound()

        val existentHoliday = holidayRepository.findByDateAndBoardId(holidayRequest.date, boardId)
        if (existentHoliday != null && existentHoliday.id != holidayId) {
            throw UniquenessFieldException("date")
        }

        holiday.updateFromHolidayRequest(holidayRequest)

        holidayRepository.save(holiday)
    }

    @Transactional
    fun clone(holidays: List<HolidayEntity>, target: BoardEntity): Unit =
        holidays.map { it.copy(id = 0, board = target) }
            .let { holidayRepository.saveAll(it) }

    @Transactional
    fun import(boardId: Long, currentUser: String) {
        val board = boardService.findById(boardId)

        val holidaysByBoard = holidayRepository.findAllByBoardId(boardId)
        val allHolidaysInCity = findAllHolidaysInCity(board, currentUser)

        if (holidaysByBoard.containsAll(allHolidaysInCity)) {
            throw HolidaysAlreadyImported(messageResolver.resolve("errors.holiday-already-imported"))
        }

        val holidays = HashSet(holidaysByBoard)
        holidays.addAll(allHolidaysInCity)

        try {
            holidayRepository.saveAll(holidays)
        } catch (e: DataIntegrityViolationException) {
            log.error("Method=importHolidays, e={}", e.message, e)
            throw HolidaysAlreadyImported(cause = e)
        }
    }

    private fun findAllHolidaysInCity(board: BoardEntity, currentUser: String): List<HolidayEntity> {
        log.info("Method=findAllHolidaysInCity, board={}, currentUser={}", board, currentUser)

        val info = userConfigService.findHolidayConfig(currentUser)

        return holidayProvider.findAllHolidays(year = LocalDate.now().year, state = info.state, city = info.city, token = info.holidayToken)
            .toHoliday(board)
    }

}
