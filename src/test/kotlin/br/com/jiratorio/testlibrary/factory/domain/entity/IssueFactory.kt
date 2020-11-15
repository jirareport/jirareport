package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.testlibrary.extension.faker.jira
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.factory.KBacon
import br.com.jiratorio.testlibrary.factory.domain.ColumnChangelogFactory
import br.com.jiratorio.repository.ColumnChangelogRepository
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import br.com.jiratorio.repository.IssueRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.TimeUnit

@Component
class IssueFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    private val columnChangelogFactory: ColumnChangelogFactory,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val persistIssue: PersistIssue?,
) : KBacon<IssueEntity>(shouldPersist = persistIssue != null) {

    override fun builder(): IssueEntity {
        return IssueEntity(
            key = "JIRAT-${faker.number().randomNumber()}",
            issueType = faker.jira().issueType(),
            creator = faker.gameOfThrones().character(),
            system = faker.jira().system(),
            epic = faker.jira().epic(),
            summary = faker.lorem().word(),
            estimate = faker.jira().estimate(),
            project = faker.jira().project(),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(10, TimeUnit.DAYS).toLocalDateTime(),
            leadTime = faker.jira().issueLeadTime(),
            created = faker.date().past(40, TimeUnit.DAYS).toLocalDateTime(),
            priority = faker.jira().priority(),
            columnChangelog = columnChangelogFactory.create(20).toSet(),
            board = boardFactory.create(),
            waitTime = faker.number().randomNumber(),
            touchTime = faker.number().randomNumber(),
            pctEfficiency = faker.number().randomDouble(2, 10, 20),
            dynamicFields = mapOf(
                "dnf_1" to faker.lorem().word(),
                "dnf_2" to faker.lorem().word()
            ),
            issuePeriodId = issuePeriodFactory.create().id
        )
    }

    override fun persist(entity: IssueEntity) {
        persistIssue?.persist(entity)
    }

    @Component
    class PersistIssue(
        private val transactionTemplate: TransactionTemplate,
        private val issueRepository: IssueRepository?,
        private val impedimentHistoryRepository: ImpedimentHistoryRepository?,
        private val columnChangelogRepository: ColumnChangelogRepository?,
    ) {

        fun persist(issue: IssueEntity) {
            transactionTemplate.execute {
                issueRepository?.save(issue)

                if (impedimentHistoryRepository != null) {
                    issue.impedimentHistory
                        .forEach { impedimentHistory ->
                            impedimentHistory.issueId = issue.id
                            impedimentHistoryRepository.save(impedimentHistory)
                        }
                }

                if (columnChangelogRepository != null) {
                    issue.columnChangelog
                        .forEach { columnChangelog ->
                            columnChangelog.issueId = issue.id
                            columnChangelogRepository.save(columnChangelog)
                        }
                }
            }
        }

    }

}
