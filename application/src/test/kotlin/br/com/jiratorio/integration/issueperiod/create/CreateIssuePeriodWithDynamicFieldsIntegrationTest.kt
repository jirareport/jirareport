package br.com.jiratorio.integration.issueperiod.create

import br.com.jiratorio.assertion.assertThat
import br.com.jiratorio.Authenticator
import br.com.jiratorio.annotation.LoadStubs
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.repository.IssueRepository
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletResponse

@IntegrationTest
internal class CreateIssuePeriodWithDynamicFieldsIntegrationTest(
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val authenticator: Authenticator,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val issueRepository: IssueRepository
) {

    @Test
    @LoadStubs(["issues/with-dynamic-fields"])
    fun `create issue period with dynamic fields`() {
        val board = authenticator.withDefaultUser {
            val board = boardFactory.create(boardFactory::withCompleteConfigurationBuilder)
            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board,
                    DynamicFieldConfigEntity::name to "Level Of Dependency",
                    DynamicFieldConfigEntity::field to "customfield_6000"
                )
            )
            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board,
                    DynamicFieldConfigEntity::name to "Team",
                    DynamicFieldConfigEntity::field to "customfield_5000"
                )
            )
            board
        }

        val request = object {
            val startDate = "28/01/2019"
            val endDate = "28/02/2019"
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/{id}/issue-periods", board.id)
            }
            then {
                statusCode(HttpServletResponse.SC_CREATED)
                header("location", Matchers.containsString("/boards/1/issue-periods/1"))
            }
        }

        val issuePeriod = issuePeriodRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        issuePeriod.assertThat {
            hasStartDate(request.startDate.toLocalDate())
            hasEndDate(request.endDate.toLocalDate())

            hasLeadTime(15.1)

            histogram.assertThat {
                hasMedian(14)
                hasPercentile75(15)
                hasPercentile90(18)
                hasChart(
                    1L to 0, 2L to 0, 3L to 0, 4L to 0, 5L to 0, 6L to 0, 7L to 0, 8L to 0, 9L to 0, 10L to 0,
                    11L to 0, 12L to 2, 13L to 2, 14L to 1, 15L to 3, 16L to 0, 17L to 0, 18L to 1, 19L to 0,
                    20L to 0, 21L to 0, 22L to 0, 23L to 0, 24L to 1
                )
            }

            hasLeadTimeByEstimate("P" to 16.25, "M" to 14.0, "G" to 15.0)
            hasThroughputByEstimate("P" to 4, "M" to 4, "G" to 2)

            hasLeadTimeBySystem("JiraReport" to 15.4, "JiraWeb" to 14.8)
            hasThroughputBySystem("JiraReport" to 5, "JiraWeb" to 5)

            hasLeadTimeByType("Task" to 15.4, "Story" to 14.5, "Attendance" to 15.0)
            hasThroughputByType("Task" to 5, "Story" to 2, "Attendance" to 3)

            hasLeadTimeByProject("Metric" to 15.714285714285714, "Estimate" to 13.666666666666666)
            hasThroughputByProject("Metric" to 7, "Estimate" to 3)

            hasLeadTimeByPriority("Major" to 17.0, "Medium" to 14.75, "Expedite" to 13.666666666666666)
            hasThroughputByPriority("Major" to 3, "Medium" to 4, "Expedite" to 3)

            hasThroughput(10)

            hasWipAvg(1.56)

            hasAvgPctEfficiency(62.19)

            hasDynamicCharts(
                listOf(
                    DynamicChart(
                        name = "Team",
                        leadTime = Chart(
                            mutableMapOf(
                                "Team A" to 16.0,
                                "Team B" to 15.333333333333334,
                                "Team C" to 13.666666666666666
                            )
                        ),
                        throughput = Chart(
                            mutableMapOf(
                                "Team A" to 4,
                                "Team B" to 3,
                                "Team C" to 3
                            )
                        )
                    ),
                    DynamicChart(
                        name = "Level Of Dependency",
                        leadTime = Chart(
                            mutableMapOf(
                                "1" to 19.5,
                                "2" to 16.5,
                                "3" to 13.166666666666666
                            )
                        ),
                        throughput = Chart(
                            mutableMapOf(
                                "1" to 2,
                                "2" to 2,
                                "3" to 6
                            )
                        )
                    )
                )
            )

            containsColumnTimeAvg(
                ColumnTimeAverageEntity(columnName = "BACKLOG", averageTime = 3.0),
                ColumnTimeAverageEntity(columnName = "ANALYSIS", averageTime = 3.2),
                ColumnTimeAverageEntity(columnName = "DEV WIP", averageTime = 2.4),
                ColumnTimeAverageEntity(columnName = "DEV DONE", averageTime = 2.6),
                ColumnTimeAverageEntity(columnName = "TEST WIP", averageTime = 2.5),
                ColumnTimeAverageEntity(columnName = "TEST DONE", averageTime = 2.8),
                ColumnTimeAverageEntity(columnName = "REVIEW", averageTime = 3.0),
                ColumnTimeAverageEntity(columnName = "ACCOMPANIMENT", averageTime = 2.5),
                ColumnTimeAverageEntity(columnName = "DONE", averageTime = 0.0)
            )

            hasEmptyLeadTimeCompareChart()
        }

        val issue = issueRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        issue.assertThat {
            hasKey("JIRAT-1")
            hasIssueType("Task")
            hasCreator("Leonardo Ferreira")
            hasSystem("JiraReport")
            hasEpic("Period")
            hasSummary("Calcular diferença de data de entrega com o primeiro due date")
            hasEstimate("P")
            hasProject("Metric")
            hasStartDate("07/01/2019 12:00".toLocalDateTime())
            hasEndDate("07/02/2019 12:00".toLocalDateTime())
            hasLeadTime(24)
            hasCreated("01/01/2019 12:00".toLocalDateTime())
            hasPriority("Major")

            hasColumnChangelog(
                ColumnChangelogEntity(
                    from = null, to = "BACKLOG", startDate = "04/01/2019 12:00".toLocalDateTime(), leadTime = 2,
                    endDate = "07/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "BACKLOG", to = "ANALYSIS", startDate = "07/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "11/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "ANALYSIS", to = "DEV WIP", startDate = "11/01/2019 12:00".toLocalDateTime(), leadTime = 2,
                    endDate = "14/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "DEV WIP", to = "DEV DONE", startDate = "14/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "19/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "DEV DONE", to = "TEST WIP", startDate = "19/01/2019 12:00".toLocalDateTime(), leadTime = 3,
                    endDate = "23/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "TEST WIP", to = "TEST DONE", startDate = "23/01/2019 12:00".toLocalDateTime(), leadTime = 4,
                    endDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "TEST DONE", to = "REVIEW", startDate = "28/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "02/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "REVIEW",
                    to = "ACCOMPANIMENT",
                    startDate = "02/02/2019 12:00".toLocalDateTime(),
                    leadTime = 4,
                    endDate = "07/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    from = "ACCOMPANIMENT", to = "DONE", startDate = "07/02/2019 12:00".toLocalDateTime(), leadTime = 0,
                    endDate = "07/02/2019 12:00".toLocalDateTime()
                )
            )

            hasDeviationOfEstimate(26)
            hasDueDateHistory(
                listOf(
                    DueDateHistory(
                        created = "06/01/2019 12:00".toLocalDateTime(),
                        dueDate = "03/01/2019".toLocalDate()
                    ),
                    DueDateHistory(
                        created = "07/01/2019 12:00".toLocalDateTime(),
                        dueDate = "06/01/2019".toLocalDate()
                    )
                )
            )

            hasImpedimentTime(0)
            hasEmptyImpedimentHistory()

            hasDynamicFields(
                mapOf(
                    "Team" to "Team A",
                    "Level Of Dependency" to "1"
                )
            )

            hasWaitTime(12242)
            hasTouchTime(22324)
            hasPctEfficiency(64.58)
        }
    }

}
