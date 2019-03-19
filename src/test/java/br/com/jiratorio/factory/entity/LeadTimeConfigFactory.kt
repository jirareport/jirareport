package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.repository.LeadTimeConfigRepository
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigFactory(
    private val faker: Faker,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val boardFactory: BoardFactory
) : JBacon<LeadTimeConfig>() {

    override fun getDefault() =
        LeadTimeConfig().apply {
            board = boardFactory.create()
            name = faker.lorem().word()
            startColumn = faker.lorem().word()
            endColumn = faker.lorem().word()
        }

    override fun getEmpty() = LeadTimeConfig()

    override fun persist(leadTimeConfig: LeadTimeConfig) {
        leadTimeConfigRepository.save(leadTimeConfig)
    }

}
