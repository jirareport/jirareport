package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.testlibrary.extension.faker.jira
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.factory.KBacon
import br.com.jiratorio.repository.LeadTimeRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LeadTimeFactory(
    private val faker: Faker,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val issueFactory: IssueFactory,
    leadTimeRepository: LeadTimeRepository?
) : KBacon<LeadTimeEntity>(leadTimeRepository) {

    override fun builder(): LeadTimeEntity {
        return LeadTimeEntity(
            leadTimeConfig = leadTimeConfigFactory.create(),
            issue = issueFactory.create(),
            leadTime = faker.jira().issueLeadTime(),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(15, TimeUnit.DAYS).toLocalDateTime()
        )
    }

}
