package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.extension.log
import br.com.jiratorio.repository.HolidayRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class FindHolidayDaysByBoard(
    private val holidayRepository: HolidayRepository
) {

    @Transactional(readOnly = true)
    fun execute(boardId: Long): List<LocalDate> {
        log.info("Method=execute, boardId={}", boardId)

        return holidayRepository
            .findAllByBoardId(boardId)
            .map { it.date }
    }

}
