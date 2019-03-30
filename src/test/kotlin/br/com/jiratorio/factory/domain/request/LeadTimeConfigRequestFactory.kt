package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigRequestFactory(
    private val faker: Faker
) : JBacon<LeadTimeConfigRequest>() {

    override fun getDefault() =
        LeadTimeConfigRequest(
            name = faker.lorem().word(),
            startColumn = faker.lorem().word(),
            endColumn = faker.lorem().word()
        )

    override fun getEmpty() =
        LeadTimeConfigRequest(
            name = faker.lorem().word(),
            startColumn = faker.lorem().word(),
            endColumn = faker.lorem().word()
        )

    override fun persist(leadTimeConfigRequest: LeadTimeConfigRequest) =
        throw UnsupportedOperationException()
}
