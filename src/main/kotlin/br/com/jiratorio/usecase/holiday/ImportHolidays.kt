package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.client.HolidayClient
import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.exception.HolidaysAlreadyImported
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toHoliday
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.usecase.userconfig.FindHolidayUserConfig
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.HashSet

@UseCase
class ImportHolidays(
    private val boardRepository: BoardRepository,
    private val messageResolver: MessageResolver,
    private val holidayRepository: HolidayRepository,
    private val holidayClient: HolidayClient,
    private val findHolidayUserConfig: FindHolidayUserConfig
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long, currentUser: Account) {
        log.info("Action=importHolidays, boardId={}", boardId)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        val holidaysByBoard = holidayRepository.findAllByBoard(board)
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

    private fun findAllHolidaysInCity(board: Board, currentUser: Account): List<Holiday> {
        log.info("Method=findAllHolidaysInCity, board={}, currentUser={}", board, currentUser)

        val info = findHolidayUserConfig.execute(currentUser.username)

        return holidayClient.findAllHolidaysInCity(
            year = LocalDate.now().year,
            state = info.state,
            city = info.city,
            token = info.holidayToken
        ).toHoliday(board)
    }

}
