package br.com.jiratorio.factory.domain.request

import br.com.leonardoferreira.jbacon.JBacon
import br.com.jiratorio.domain.request.CreateBoardRequest
import com.github.javafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateBoardRequestFactory(val faker: Faker) : JBacon<CreateBoardRequest>() {

    override fun getDefault(): CreateBoardRequest {
        return CreateBoardRequest().apply {
            name = faker.lorem().word()
            externalId = faker.number().randomNumber()
        }
    }

    override fun getEmpty() =
            CreateBoardRequest()

    override fun persist(createBoardRequest: CreateBoardRequest) =
            throw UnsupportedOperationException()

}
