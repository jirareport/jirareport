package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HolidayRequestFactory(
    private val faker: Faker
) : KBacon<HolidayRequest>() {

    override fun builder(): HolidayRequest {
        return HolidayRequest(
            date = faker.date().future(5, TimeUnit.DAYS).toLocalDate(),
            description = faker.lorem().word()
        )
    }

}
