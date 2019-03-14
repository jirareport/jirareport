package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.extension.format
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HolidayRequestFactory(
        private val faker: Faker
) : JBacon<HolidayRequest>() {

    override fun getDefault() =
            HolidayRequest().apply {
                date = faker.date().future(5, TimeUnit.DAYS).format("dd/MM/yyyy")
                description = faker.lorem().word()
            }

    override fun getEmpty() =
            HolidayRequest()

    override fun persist(holidayRequest: HolidayRequest) =
            throw UnsupportedOperationException()
}
