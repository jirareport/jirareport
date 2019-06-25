package br.com.jiratorio.integration.issueperiod.create

import br.com.jiratorio.assert.IssueAssert
import br.com.jiratorio.assert.IssuePeriodAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.repository.IssueRepository
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import javax.servlet.http.HttpServletResponse

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateCompleteIssuePeriodIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val issueRepository: IssueRepository,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val transactionTemplate: TransactionTemplate
) {

    @Test
    @LoadStubs(["issues/complete-issues"])
    fun `create complete issue period`() {
        val board = authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create(boardFactory::withCompleteConfigurationBuilder)

            leadTimeConfigFactory.create {
                it.name = "Dev Lead Time"
                it.startColumn = "DEV WIP"
                it.endColumn = "DEV DONE"
                it.board = defaultBoard
            }

            leadTimeConfigFactory.create {
                it.name = "Test Lead Time"
                it.startColumn = "TEST WIP"
                it.endColumn = "TEST DONE"
                it.board = defaultBoard
            }

            defaultBoard
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

            hasLeadTime(15.9)

            histogram().assertThat {
                hasMedian(15)
                hasPercentile75(19)
                hasPercentile90(20)
                hasChart(
                    1L to 0,
                    2L to 0,
                    3L to 0,
                    4L to 0,
                    5L to 0,
                    6L to 0,
                    7L to 0,
                    8L to 0,
                    9L to 0,
                    10L to 0,
                    11L to 1,
                    12L to 1,
                    13L to 0,
                    14L to 2,
                    15L to 2,
                    16L to 0,
                    17L to 1,
                    18L to 0,
                    19L to 1,
                    20L to 1,
                    21L to 0,
                    22L to 1
                )
            }

            hasLeadTimeByEstimate("P" to 19.5, "M" to 12.75, "G" to 15.0)
            hasThroughputByEstimate("P" to 4, "M" to 4, "G" to 2)

            hasLeadTimeBySystem("JiraReport" to 16.2, "JiraWeb" to 15.6)
            hasThroughputBySystem("JiraReport" to 5, "JiraWeb" to 5)

            hasLeadTimeByType("Task" to 16.2, "Story" to 17.0, "Attendance" to 14.666666666666666)
            hasThroughputByType("Task" to 5, "Story" to 2, "Attendance" to 3)

            hasLeadTimeByProject("Metric" to 15.714285714285714, "Estimate" to 16.333333333333332)
            hasThroughputByProject("Metric" to 7, "Estimate" to 3)

            hasLeadTimeByPriority("Major" to 19.333333333333332, "Medium" to 16.0, "Expedite" to 12.333333333333334)
            hasThroughputByPriority("Major" to 3, "Medium" to 4, "Expedite" to 3)

            hasThroughput(10)

            hasWipAvg(1.75)

            hasAvgPctEfficiency(70.3)

            hasEmptyDynamicCharts()

            containsColumnTimeAvg(
                ColumnTimeAvg(columnName = "BACKLOG", avgTime = 2.1),
                ColumnTimeAvg(columnName = "ANALYSIS", avgTime = 3.0),
                ColumnTimeAvg(columnName = "DEV WIP", avgTime = 3.7),
                ColumnTimeAvg(columnName = "DEV DONE", avgTime = 2.3),
                ColumnTimeAvg(columnName = "TEST WIP", avgTime = 2.8),
                ColumnTimeAvg(columnName = "TEST DONE", avgTime = 2.7),
                ColumnTimeAvg(columnName = "REVIEW", avgTime = 2.3),
                ColumnTimeAvg(columnName = "ACCOMPANIMENT", avgTime = 3.3),
                ColumnTimeAvg(columnName = "DONE", avgTime = 0.0)
            )

            hasLeadTimeCompareChart(
                mapOf(
                    "Dev Lead Time" to 3.7,
                    "Test Lead Time" to 2.8
                )
            )
        }

        transactionTemplate.execute {
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
                hasStartDate("04/01/2019 12:00".toLocalDateTime())
                hasEndDate("30/01/2019 12:00".toLocalDateTime())
                hasLeadTime(19)
                hasCreated("01/01/2019 12:00".toLocalDateTime())
                hasPriority("Major")

                hasChangelog(
                    Changelog(
                        from = null,
                        to = "BACKLOG",
                        created = "03/01/2019 12:00".toLocalDateTime(),
                        leadTime = 2,
                        endDate = "04/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "BACKLOG",
                        to = "ANALYSIS",
                        created = "04/01/2019 12:00".toLocalDateTime(),
                        leadTime = 2,
                        endDate = "07/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "ANALYSIS",
                        to = "DEV WIP",
                        created = "07/01/2019 12:00".toLocalDateTime(),
                        leadTime = 5,
                        endDate = "12/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "DEV WIP",
                        to = "DEV DONE",
                        created = "12/01/2019 12:00".toLocalDateTime(),
                        leadTime = 2,
                        endDate = "15/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "DEV DONE",
                        to = "TEST WIP",
                        created = "15/01/2019 12:00".toLocalDateTime(),
                        leadTime = 4,
                        endDate = "20/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "TEST WIP",
                        to = "TEST DONE",
                        created = "20/01/2019 12:00".toLocalDateTime(),
                        leadTime = 4,
                        endDate = "24/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "TEST DONE",
                        to = "REVIEW",
                        created = "24/01/2019 12:00".toLocalDateTime(),
                        leadTime = 3,
                        endDate = "28/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "REVIEW",
                        to = "ACCOMPANIMENT",
                        created = "28/01/2019 12:00".toLocalDateTime(),
                        leadTime = 3,
                        endDate = "30/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        from = "ACCOMPANIMENT",
                        to = "DONE",
                        created = "30/01/2019 12:00".toLocalDateTime(),
                        leadTime = 0,
                        endDate = "30/01/2019 12:00".toLocalDateTime()
                    )
                )

                hasDeviationOfEstimate(8)
                hasDueDateHistory(
                    listOf(
                        DueDateHistory(
                            created = "04/01/2019 12:00".toLocalDateTime(),
                            dueDate = "19/01/2019".toLocalDate()
                        )
                    )
                )

                hasImpedimentTime(3)
                containsImpedimentHistory(
                    ImpedimentHistory(
                        id = 1,
                        issueId = 1,
                        startDate = "16/01/2019 12:00".toLocalDateTime(),
                        endDate = "18/01/2019 12:00".toLocalDateTime(),
                        leadTime = 3
                    )
                )

                hasEmptyDynamicFields()

                hasWaitTime(8643)
                hasTouchTime(18723)
                hasPctEfficiency(68.42)

                hasLeadTimes(
                    setOf(
                        LeadTime(
                            leadTimeConfig = LeadTimeConfig(
                                board = board,
                                name = "Test Lead Time",
                                startColumn = "TEST WIP",
                                endColumn = "TEST DONE"
                            ),
                            leadTime = 4,
                            startDate = "15/01/2019 12:00".toLocalDateTime(),
                            endDate = "20/01/2019 12:00".toLocalDateTime(),
                            issue = issue
                        ),
                        LeadTime(
                            leadTimeConfig = LeadTimeConfig(
                                board = board,
                                name = "Dev Lead Time",
                                startColumn = "DEV WIP",
                                endColumn = "DEV DONE"
                            ),
                            leadTime = 5,
                            startDate = "07/01/2019 12:00".toLocalDateTime(),
                            endDate = "12/01/2019 12:00".toLocalDateTime(),
                            issue = issue
                        )
                    )
                )
            }
        }
    }
}
