package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.factory.KBacon
import br.com.jiratorio.repository.ColumnTimeAverageRepository
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class ColumnTimeAverageFactory(
    private val faker: Faker,
    private val issuePeriodFactory: IssuePeriodFactory,
    columnTimeAverageRepository: ColumnTimeAverageRepository?
) : KBacon<ColumnTimeAverageEntity>(columnTimeAverageRepository) {

    override fun builder(): ColumnTimeAverageEntity =
        ColumnTimeAverageEntity(
            issuePeriodId = issuePeriodFactory.create().id,
            columnName = faker.jira().column(),
            averageTime = faker.jira().leadTime()
        )

}
