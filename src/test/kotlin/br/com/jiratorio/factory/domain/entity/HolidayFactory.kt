package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.repository.HolidayRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HolidayFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    holidayRepository: HolidayRepository?
) : KBacon<Holiday>(holidayRepository) {

    override fun builder(): Holiday {
        return Holiday(
            date = faker.date().future(5, TimeUnit.DAYS).toLocalDate(),
            description = faker.lorem().word(),
            board = boardFactory.create()
        )
    }

}
