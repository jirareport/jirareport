package br.com.jiratorio.factory.domain.entity

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.extension.faker.jira
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
            leadTime = faker.jira().leadTime(),
            wipAvg = faker.number().randomDouble(2, 1, 10),
            avgPctEfficiency = faker.number().randomDouble(2, 1, 10),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDate(),
            endDate = faker.date().past(15, TimeUnit.DAYS).toLocalDate(),
            jql = faker.lorem().paragraph(),
            issues = mutableListOf(),
            issuesCount = faker.number().randomNumber().toInt(),
            leadTimeByEstimate = Chart(
                data = mapOf(
                    "P" to faker.jira().leadTime(),
                    "M" to faker.jira().leadTime(),
                    "G" to faker.jira().leadTime()
                )
            ),
            throughputByEstimate = Chart(
                data = mapOf(
                    "P" to faker.jira().throughput(),
                    "M" to faker.jira().throughput(),
                    "G" to faker.jira().throughput()
                )
            ),
            leadTimeCompareChart = Chart(
                data = mapOf(
                    "Dev Lead Time" to faker.jira().leadTime(),
                    "Test Lead Time" to faker.jira().leadTime(),
                    "Delivery Lead Time" to faker.jira().leadTime()
                )
            ),
            leadTimeBySystem = Chart(
                data = mapOf(
                    "jirareport-api" to faker.jira().leadTime(),
                    "jirareport-web" to faker.jira().leadTime()
                )
            ),
            throughputBySystem = Chart(
                data = mapOf(
                    "jirareport-api" to faker.jira().throughput(),
                    "jirareport-web" to faker.jira().throughput()
                )
            ),
            leadTimeByType = Chart(
                data = mapOf(
                    "story" to faker.jira().leadTime(),
                    "subTask" to faker.jira().leadTime()
                )
            ),
            throughputByType = Chart(
                data = mapOf(
                    "story" to faker.jira().throughput(),
                    "subTask" to faker.jira().throughput()
                )
            ),
            leadTimeByProject = Chart(
                data = mapOf(
                    "jirareport" to faker.jira().leadTime()
                )
            ),
            throughputByProject = Chart(
                data = mapOf(
                    "jirareport" to faker.jira().throughput()
                )
            ),
            leadTimeByPriority = Chart(
                data = mapOf(
                    "medium" to faker.jira().leadTime(),
                    "major" to faker.jira().leadTime(),
                    "expedite" to faker.jira().leadTime()
                )
            ),
            throughputByPriority = Chart(
                data = mapOf(
                    "medium" to faker.jira().throughput(),
                    "major" to faker.jira().throughput(),
                    "expedite" to faker.jira().throughput()
                )
            ),
            columnTimeAvg = mutableListOf(
                ColumnTimeAvg("TODO", faker.jira().leadTime()),
                ColumnTimeAvg("WIP", faker.jira().leadTime()),
                ColumnTimeAvg("DONE", 0.0)
            ),
            dynamicCharts = mutableListOf(
                DynamicChart(
                    name = "dnf_1",
                    leadTime = Chart(
                        data = mapOf(
                            "value1" to faker.jira().leadTime(),
                            "value2" to faker.jira().leadTime()
                        )
                    ),
                    throughput = Chart(
                        data = mapOf(
                            "value1" to faker.jira().throughput(),
                            "value2" to faker.jira().throughput()
                        )
                    )
                ),
                DynamicChart(
                    name = "dnf_2",
                    leadTime = Chart(
                        data = mapOf(
                            "value1" to faker.jira().leadTime(),
                            "value2" to faker.jira().leadTime()
                        )
                    ),
                    throughput = Chart(
                        data = mapOf(
                            "value1" to faker.jira().throughput(),
                            "value2" to faker.jira().throughput()
                        )
                    )
                )
            )
        )
    }

}
