package br.com.jiratorio.service.impl

import br.com.jiratorio.client.HolidayClient
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.HolidayResponse
import br.com.jiratorio.exception.HolidaysAlreadyImported
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.HolidayMapper
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.UserConfigService
import br.com.jiratorio.util.logger
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.HashSet

@Service
class HolidayServiceImpl(
        private val holidayRepository: HolidayRepository,
        private val boardService: BoardService,
        private val holidayClient: HolidayClient,
        private val holidayMapper: HolidayMapper,
        private val userConfigService: UserConfigService
) : HolidayService {

    private val log = logger()

    @Transactional(readOnly = true)
    override fun findByBoard(boardId: Long, pageable: Pageable): Page<HolidayResponse> {
        log.info("Method=findByBoard, boardId={}", boardId)

        val holidays = holidayRepository.findAllByBoardId(boardId, pageable)
        return holidayMapper.toHolidayResponse(holidays)
    }

    @Transactional(readOnly = true)
    override fun findDaysByBoard(boardId: Long): List<LocalDate> {
        log.info("Method=findDaysByBoard, boardId={}", boardId)

        return holidayRepository.findAllByBoardId(boardId)
                .map { it.date!! }
    }

    @Transactional
    override fun create(boardId: Long, holidayRequest: HolidayRequest): Long {
        log.info("Method=create, boardId={}, holidayRequest={}", boardId, holidayRequest)

        val board = boardService.findById(boardId)

        val holiday = holidayMapper.toHoliday(holidayRequest, board)
        holidayRepository.save(holiday)

        return holiday.id!!
    }

    @Transactional
    override fun delete(id: Long) {
        log.info("Method=delete, id={}", id)

        val holiday = holidayRepository.findById(id)
                .orElseThrow(::ResourceNotFound)

        holidayRepository.delete(holiday)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): HolidayResponse {
        log.info("Method=findById, id={}", id)

        val holiday = holidayRepository.findById(id)
                .orElseThrow(::ResourceNotFound)

        return holidayMapper.toHolidayResponse(holiday)
    }

    @Transactional
    override fun update(boardId: Long, holidayId: Long, holidayRequest: HolidayRequest) {
        log.info("Method=update, boardId={}, holidayId={}, holidayRequest={}", boardId, holidayId, holidayRequest)

        val board = boardService.findById(boardId)

        val holiday = holidayRepository.findById(holidayId)
                .filter { it.board == board }
                .orElseThrow(::ResourceNotFound)

        holidayMapper.updateFromRequest(holiday, holidayRequest)

        holidayRepository.save(holiday)
    }

    @Transactional
    override fun createImported(boardId: Long, currentUser: Account) {
        log.info("Method=createImported, boardId={}", boardId)

        val board = boardService.findById(boardId)

        val holidaysByBoard = holidayRepository.findAllByBoard(board)
        val allHolidaysInCity = findAllHolidaysInCity(board, currentUser)

        if (holidaysByBoard.containsAll(allHolidaysInCity)) {
            throw HolidaysAlreadyImported()
        }

        val holidays = HashSet(holidaysByBoard)
        holidays.addAll(allHolidaysInCity)

        try {
            holidayRepository.saveAll(holidays)
        } catch (e: DataIntegrityViolationException) {
            log.error("Method=createImported, e={}", e.message, e)
            throw HolidaysAlreadyImported(e)
        }
    }

    private fun findAllHolidaysInCity(board: Board, currentUser: Account): List<Holiday> {
        log.info("Method=findAllHolidaysInCity, board={}, currentUser={}", board, currentUser)

        val info = userConfigService.retrieveHolidayInfo(currentUser.username)

        val allHolidaysInCity = holidayClient.findAllHolidaysInCity(
                LocalDate.now().year, info.state, info.city, info.holidayToken)

        return holidayMapper.fromApiResponse(allHolidaysInCity, board)
    }
}
