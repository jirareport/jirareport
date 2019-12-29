package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.mapper.toHolidayResponse
import br.com.jiratorio.repository.HolidayRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindHolidayByBoard(
    private val holidayRepository: HolidayRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long, pageable: Pageable): Page<HolidayResponse> {
        log.info("Method=execute, boardId={}", boardId)

        return holidayRepository
            .findAllByBoardId(boardId, pageable)
            .toHolidayResponse()
    }

}
