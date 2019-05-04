package br.com.jiratorio.integration.issueperiod.create

import br.com.jiratorio.assert.IssueAssert
import br.com.jiratorio.assert.IssuePeriodAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
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
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.servlet.http.HttpServletResponse

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateIssuePeriodWithDynamicFieldsIntegrationTest @Autowired constructor(
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
            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "Level Of Dependency"
                it.field = "customfield_6000"
            }
            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "Team"
                it.field = "customfield_5000"
            }
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

        val issuePeriod = issuePeriodRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        IssuePeriodAssert(issuePeriod).assertThat {
            hasStartDate(request.startDate.toLocalDate())
            hasEndDate(request.endDate.toLocalDate())

            hasLeadTime(15.1)

            histogram().assertThat {
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
                ColumnTimeAvg(columnName = "BACKLOG", avgTime = 3.0),
                ColumnTimeAvg(columnName = "ANALYSIS", avgTime = 3.2),
                ColumnTimeAvg(columnName = "DEV WIP", avgTime = 2.4),
                ColumnTimeAvg(columnName = "DEV DONE", avgTime = 2.6),
                ColumnTimeAvg(columnName = "TEST WIP", avgTime = 2.5),
                ColumnTimeAvg(columnName = "TEST DONE", avgTime = 2.8),
                ColumnTimeAvg(columnName = "REVIEW", avgTime = 3.0),
                ColumnTimeAvg(columnName = "ACCOMPANIMENT", avgTime = 2.5),
                ColumnTimeAvg(columnName = "DONE", avgTime = 0.0)
            )

            hasEmptyLeadTimeCompareChart()
        }

        val issue = issueRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        IssueAssert(issue).assertThat {
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

            hasChangelog(
                Changelog(
                    from = null, to = "BACKLOG", created = "04/01/2019 12:00".toLocalDateTime(), leadTime = 2,
                    endDate = "07/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "BACKLOG", to = "ANALYSIS", created = "07/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "11/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "ANALYSIS", to = "DEV WIP", created = "11/01/2019 12:00".toLocalDateTime(), leadTime = 2,
                    endDate = "14/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "DEV WIP", to = "DEV DONE", created = "14/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "19/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "DEV DONE", to = "TEST WIP", created = "19/01/2019 12:00".toLocalDateTime(), leadTime = 3,
                    endDate = "23/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "TEST WIP", to = "TEST DONE", created = "23/01/2019 12:00".toLocalDateTime(), leadTime = 4,
                    endDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "TEST DONE", to = "REVIEW", created = "28/01/2019 12:00".toLocalDateTime(), leadTime = 5,
                    endDate = "02/02/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "REVIEW", to = "ACCOMPANIMENT", created = "02/02/2019 12:00".toLocalDateTime(), leadTime = 4,
                    endDate = "07/02/2019 12:00".toLocalDateTime()
                ),
                Changelog(
                    from = "ACCOMPANIMENT", to = "DONE", created = "07/02/2019 12:00".toLocalDateTime(), leadTime = 0,
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
