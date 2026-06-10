package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.testlibrary.extension.faker.jira
import br.com.jiratorio.testlibrary.extension.toLocalDate
import br.com.jiratorio.testlibrary.factory.KBacon
import br.com.jiratorio.repository.IssuePeriodRepository
import net.datafaker.Faker
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Component
class IssuePeriodFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    issuePeriodRepository: IssuePeriodRepository?
) : KBacon<IssuePeriodEntity>(issuePeriodRepository) {

    override fun builder(): IssuePeriodEntity {
        val board = boardFactory.create()
        val startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDate()
        val endDate = faker.date().past(15, TimeUnit.DAYS).toLocalDate()

        return IssuePeriodEntity(
            board = board,
            leadTime = faker.jira().leadTime(),
            wipAvg = faker.number().randomDouble(2, 1, 10),
            avgPctEfficiency = faker.number().randomDouble(2, 1, 10),
            startDate = startDate,
            endDate = endDate,
            name = "[${startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}]",
            jql = faker.lorem().paragraph(),
            issues = mutableSetOf(),
            throughput = faker.number().randomNumber().toInt(),
        )
    }

}
