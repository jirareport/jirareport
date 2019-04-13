package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.repository.IssuePeriodRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class IssuePeriodFactory(
    private val faker: Faker,
    private val boardFactory: BoardFactory,
    issuePeriodRepository: IssuePeriodRepository?
) : KBacon<IssuePeriod>(issuePeriodRepository) {

    override fun builder(): IssuePeriod {
        return IssuePeriod(
            boardId = boardFactory.create().id,
            leadTime = faker.number().randomDouble(2, 1, 10),
            wipAvg = faker.number().randomDouble(2, 1, 10),
            avgPctEfficiency = faker.number().randomDouble(2, 1, 10),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDate(),
            endDate = faker.date().past(15, TimeUnit.DAYS).toLocalDate(),
            jql = faker.lorem().paragraph(),
            issues = mutableListOf(),
            issuesCount = faker.number().randomNumber().toInt(),
            throughputByEstimate = Chart(
                data = mutableMapOf(
                    "P" to faker.number().randomNumber().toInt(),
                    "M" to faker.number().randomNumber().toInt(),
                    "G" to faker.number().randomNumber().toInt()
                )
            ),
            leadTimeCompareChart = Chart(
                data = mutableMapOf(
                    "Dev Lead Time" to faker.number().randomDouble(2, 1, 10),
                    "Test Lead Time" to faker.number().randomDouble(2, 1, 10),
                    "Delivery Lead Time" to faker.number().randomDouble(2, 1, 10)
                )
            )
        )
    }

}
