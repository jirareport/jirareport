package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.DynamicFieldConfig
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
) : KBacon<DynamicFieldConfig>(dynamicFieldConfigRepository) {

    override fun builder(): DynamicFieldConfig {
        return DynamicFieldConfig(
            board = boardFactory.create(),
            name = faker.lorem().word(),
            field = faker.jira().customField()
        )
    }

}
