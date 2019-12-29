package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.repository.HolidayRepository
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteHoliday(
    private val holidayRepository: HolidayRepository
) {

    @Transactional
    fun execute(id: Long) {
        log.info("Method=execute, id={}", id)

        val holiday = holidayRepository.findByIdOrNull(id)
            ?: throw ResourceNotFound()

        holidayRepository.delete(holiday)
    }

}
