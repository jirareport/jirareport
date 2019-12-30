package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.HolidayRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteHoliday(
    private val holidayRepository: HolidayRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long) {
        log.info("Method=execute, id={}", id)

        val holiday = holidayRepository.findByIdOrNull(id)
            ?: throw ResourceNotFound()

        holidayRepository.delete(holiday)
    }

}
