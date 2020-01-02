package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IssueFiltersIntegrationTest(
    private val issueFactory: IssueFactory,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory
) {

    @Test
    fun `test find filters`() {
        buildIssues()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issues/filters")
            }
            then {
                statusCode(SC_OK)
                body("estimates", containsInAnyOrder("P", "M", "G"))
                body("systems", containsInAnyOrder("jirareport-web", "jirareport-api"))
                body("epics", containsInAnyOrder("v1", "v2", "v3"))
                body("issueTypes", containsInAnyOrder("Story", "Task"))
                body("projects", containsInAnyOrder("JiraReport On Premise", "JiraReport SASS"))
                body("priorities", containsInAnyOrder("Medium", "Major", "Expedite", "Blocker", "Low"))
                body(
                    "dynamicFieldsValues.find { it.field == 'field1' }.values",
                    containsInAnyOrder("value1", "value2", "value3")
                )
                body(
                    "dynamicFieldsValues.find { it.field == 'field2' }.values",
                    containsInAnyOrder("value1", "value2", "value3")
                )
            }
        }
    }

    @Test
    fun `test find filter keys`() {
        val (startDateFilter, endDateFilter) = buildIssues()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", startDateFilter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                param("endDate", endDateFilter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            }
            on {
                get("/boards/1/issues/filters/keys")
            }
            then {
                statusCode(SC_OK)
                body("keys", Matchers.hasSize<Int>(10))
                body(
                    "keys", containsInAnyOrder(
                        "JIRAT-1",
                        "JIRAT-2",
                        "JIRAT-3",
                        "JIRAT-4",
                        "JIRAT-5",
                        "JIRAT-6",
                        "JIRAT-7",
                        "JIRAT-8",
                        "JIRAT-9",
                        "JIRAT-10"
                    )
                )
            }
        }
    }

    private fun buildIssues(): Pair<LocalDate, LocalDate> {
        val startDateFilter = "01/01/2019".toLocalDate()
        val endDateFilter = "05/04/2019".toLocalDate()
        val dateRange = (startDateFilter..endDateFilter).toList()

        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()
            val issuePeriod = issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to defaultBoard
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-1",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "P",
                    Issue::system to "jirareport-api",
                    Issue::epic to "v1",
                    Issue::issueType to "Story",
                    Issue::project to "JiraReport On Premise",
                    Issue::priority to "Major",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value1"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-2",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "P",
                    Issue::system to "jirareport-api",
                    Issue::epic to "v1",
                    Issue::issueType to "Story",
                    Issue::project to "JiraReport On Premise",
                    Issue::priority to "Major",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value1"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-3",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "M",
                    Issue::system to "jirareport-api",
                    Issue::epic to "v1",
                    Issue::issueType to "Story",
                    Issue::project to "JiraReport On Premise",
                    Issue::priority to "Major",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value2"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-4",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "M",
                    Issue::system to "jirareport-api",
                    Issue::epic to "v2",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Medium",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value1"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-5",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "M",
                    Issue::system to "jirareport-api",
                    Issue::epic to "v2",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Medium",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value2",
                        "field2" to "value1"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-6",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "M",
                    Issue::system to "jirareport-web",
                    Issue::epic to "v3",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Medium",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value2"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-7",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "M",
                    Issue::system to "jirareport-web",
                    Issue::epic to "v3",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Expedite",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value3"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-8",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "G",
                    Issue::system to "jirareport-web",
                    Issue::epic to "v3",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Expedite",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value2",
                        "field2" to "value1"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-9",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "G",
                    Issue::system to "jirareport-web",
                    Issue::epic to "v3",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Blocker",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value3"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-10",
                    Issue::board to defaultBoard,
                    Issue::endDate to dateRange.random().atTime(12, 0, 0),
                    Issue::estimate to "P",
                    Issue::system to "jirareport-web",
                    Issue::epic to "v3",
                    Issue::issueType to "Task",
                    Issue::project to "JiraReport SASS",
                    Issue::priority to "Low",
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value2"
                    ),
                    Issue::issuePeriodId to issuePeriod.id
                )
            )
        }

        return startDateFilter to endDateFilter
    }

}
