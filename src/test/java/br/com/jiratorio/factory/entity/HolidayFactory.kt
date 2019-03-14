package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.util.lazyInitBy
import br.com.jiratorio.repository.HolidayRepository
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HolidayFactory(
        private val faker: Faker,
        private val holidayRepository: HolidayRepository,
        private val boardFactory: BoardFactory
) : JBacon<Holiday>() {

    override fun getDefault() =
            Holiday().apply {
                date = faker.date().future(5, TimeUnit.DAYS).toLocalDate()
                description = faker.lorem().word()
                board = lazyInitBy { boardFactory.create() }
            }

    override fun getEmpty() = Holiday()

    override fun persist(holiday: Holiday) {
        holidayRepository.save(holiday)
    }

}
