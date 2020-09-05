package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ImpedimentHistoryFactory(
    private val faker: Faker,
    impedimentHistoryRepository: ImpedimentHistoryRepository?
) : KBacon<ImpedimentHistoryEntity>(impedimentHistoryRepository) {

    override fun builder(): ImpedimentHistoryEntity {
        return ImpedimentHistoryEntity(
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(15, TimeUnit.DAYS).toLocalDateTime(),
            leadTime = faker.jira().issueLeadTime()
        )
    }

}
