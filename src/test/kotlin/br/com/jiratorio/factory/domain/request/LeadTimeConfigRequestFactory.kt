package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigRequestFactory(
    private val faker: Faker
) : KBacon<LeadTimeConfigRequest>() {

    override fun builder(): LeadTimeConfigRequest {
        return LeadTimeConfigRequest(
            name = faker.lorem().word(),
            startColumn = faker.lorem().word(),
            endColumn = faker.lorem().word()
        )
    }

}
