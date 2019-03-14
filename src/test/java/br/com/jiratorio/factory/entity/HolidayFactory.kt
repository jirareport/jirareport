package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.repository.HolidayRepository
import br.com.leonardoferreira.jbacon.JBacon
import br.com.leonardoferreira.jbacon.Lazy
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Component
class HolidayFactory(
        val faker: Faker,
        val holidayRepository: HolidayRepository,
        val boardFactory: BoardFactory
) : JBacon<Holiday>() {

    override fun getDefault() =
            Holiday().apply {
                date = faker.date().future(5, TimeUnit.DAYS).toLocalDate()
                description = faker.lorem().word()
                val lazyBoard by lazy { boardFactory.create() }
                board = lazyBoard
            }

    override fun getEmpty() = Holiday()

    override fun persist(holiday: Holiday) {
        holidayRepository.save(holiday)
    }

}
