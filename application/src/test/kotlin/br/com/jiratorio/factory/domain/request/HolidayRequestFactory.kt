package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HolidayRequestFactory(
    private val faker: Faker
) : KBacon<HolidayRequest>() {

    override fun builder(): HolidayRequest {
        return HolidayRequest(
            date = LocalDate.now().plusDays(faker.number().randomNumber()),
            description = faker.lorem().word()
        )
    }

}
