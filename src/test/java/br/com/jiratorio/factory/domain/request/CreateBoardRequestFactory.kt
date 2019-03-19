package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class CreateBoardRequestFactory(
    private val faker: Faker
) : JBacon<CreateBoardRequest>() {

    override fun getDefault() =
        CreateBoardRequest().apply {
            name = faker.lorem().word()
            externalId = faker.number().randomNumber()
        }

    override fun getEmpty() =
        CreateBoardRequest()

    override fun persist(createBoardRequest: CreateBoardRequest) =
        throw UnsupportedOperationException()

}
