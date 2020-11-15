package br.com.jiratorio.testlibrary.factory.domain.request

import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.testlibrary.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class CreateBoardRequestFactory(
    private val faker: Faker
) : KBacon<CreateBoardRequest>() {

    override fun builder(): CreateBoardRequest {
        return CreateBoardRequest(
            name = faker.lorem().word(),
            externalId = faker.number().randomNumber()
        )
    }

}
