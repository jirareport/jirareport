package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.testlibrary.factory.KBacon
import br.com.jiratorio.repository.LeadTimeConfigRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    leadTimeConfigRepository: LeadTimeConfigRepository?
) : KBacon<LeadTimeConfigEntity>(leadTimeConfigRepository) {

    override fun builder(): LeadTimeConfigEntity {
        return LeadTimeConfigEntity(
            board = boardFactory.create(),
            name = faker.lorem().word(),
            startColumn = faker.lorem().word(),
            endColumn = faker.lorem().word()
        )
    }

}
