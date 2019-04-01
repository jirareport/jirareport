package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.factory.domain.ChangelogFactory
import br.com.jiratorio.repository.IssueRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class IssueFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    private val issueRepository: IssueRepository,
    private val changelogFactory: ChangelogFactory
) : KBacon<Issue>() {

    override fun builder(): Issue {
        return Issue(
            key = "JIRAT-${faker.number().randomNumber()}",
            issueType = faker.jira().issueType(),
            creator = faker.gameOfThrones().character(),
            system = faker.jira().system(),
            epic = faker.jira().epic(),
            summary = faker.lorem().paragraph(),
            estimated = faker.jira().estimate(),
            project = faker.jira().project(),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(10, TimeUnit.DAYS).toLocalDateTime(),
            leadTime = faker.number().randomNumber(),
            created = faker.date().past(40, TimeUnit.DAYS).toLocalDateTime(),
            priority = faker.jira().priority(),
            changelog = changelogFactory.create(20, persist = false),
            board = boardFactory.create(),
            waitTime = faker.number().randomNumber(),
            touchTime = faker.number().randomNumber(),
            pctEfficiency = faker.number().randomDouble(2, 10, 20),
            dynamicFields = mapOf(

            )
        )
    }

    override fun persist(entity: Issue) {
        issueRepository.save(entity)
    }


}
