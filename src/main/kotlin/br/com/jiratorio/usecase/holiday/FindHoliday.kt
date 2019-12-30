package br.com.jiratorio.usecase.holiday

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toHolidayResponse
import br.com.jiratorio.repository.HolidayRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindHoliday(
    private val holidayRepository: HolidayRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(id: Long): HolidayResponse {
        log.info("Action=findHoliday, id={}", id)

        val holiday = holidayRepository.findByIdOrNull(id)
            ?: throw ResourceNotFound()

        return holiday.toHolidayResponse()
    }

}
