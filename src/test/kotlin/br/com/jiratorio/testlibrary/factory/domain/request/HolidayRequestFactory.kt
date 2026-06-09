package br.com.jiratorio.testlibrary.factory.domain.request

import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.testlibrary.factory.KBacon
import net.datafaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HolidayRequestFactory(
    private val faker: Faker
) : KBacon<HolidayRequest>() {

    override fun builder(): HolidayRequest {
        return HolidayRequest(
            date = LocalDate.now().plusDays(faker.number().numberBetween(1L, 365L * 5)),
            description = faker.lorem().word()
        )
    }

}
