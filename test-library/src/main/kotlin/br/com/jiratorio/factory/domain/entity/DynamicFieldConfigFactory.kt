package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class DynamicFieldConfigFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    dynamicFieldConfigRepository: DynamicFieldConfigRepository?
) : KBacon<DynamicFieldConfigEntity>(dynamicFieldConfigRepository) {

    override fun builder(): DynamicFieldConfigEntity {
        return DynamicFieldConfigEntity(
            board = boardFactory.create(),
            name = faker.lorem().word(),
            field = faker.jira().customField()
        )
    }

}
