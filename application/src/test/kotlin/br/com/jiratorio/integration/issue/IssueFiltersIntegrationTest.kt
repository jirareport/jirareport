package br.com.jiratorio.integration.issue

import br.com.jiratorio.Authenticator
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@IntegrationTest
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
                    "keys",
                    containsInAnyOrder(
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
                    IssuePeriodEntity::board to defaultBoard
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-1",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "P",
                    IssueEntity::system to "jirareport-api",
                    IssueEntity::epic to "v1",
                    IssueEntity::issueType to "Story",
                    IssueEntity::project to "JiraReport On Premise",
                    IssueEntity::priority to "Major",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value1"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-2",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "P",
                    IssueEntity::system to "jirareport-api",
                    IssueEntity::epic to "v1",
                    IssueEntity::issueType to "Story",
                    IssueEntity::project to "JiraReport On Premise",
                    IssueEntity::priority to "Major",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value1"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-3",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "M",
                    IssueEntity::system to "jirareport-api",
                    IssueEntity::epic to "v1",
                    IssueEntity::issueType to "Story",
                    IssueEntity::project to "JiraReport On Premise",
                    IssueEntity::priority to "Major",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value2"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-4",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "M",
                    IssueEntity::system to "jirareport-api",
                    IssueEntity::epic to "v2",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Medium",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value1"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-5",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "M",
                    IssueEntity::system to "jirareport-api",
                    IssueEntity::epic to "v2",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Medium",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value2",
                        "field2" to "value1"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-6",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "M",
                    IssueEntity::system to "jirareport-web",
                    IssueEntity::epic to "v3",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Medium",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value2"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-7",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "M",
                    IssueEntity::system to "jirareport-web",
                    IssueEntity::epic to "v3",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Expedite",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value3"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-8",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "G",
                    IssueEntity::system to "jirareport-web",
                    IssueEntity::epic to "v3",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Expedite",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value2",
                        "field2" to "value1"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-9",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "G",
                    IssueEntity::system to "jirareport-web",
                    IssueEntity::epic to "v3",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Blocker",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value3"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )

            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-10",
                    IssueEntity::board to defaultBoard,
                    IssueEntity::endDate to dateRange.random().atTime(12, 0, 0),
                    IssueEntity::estimate to "P",
                    IssueEntity::system to "jirareport-web",
                    IssueEntity::epic to "v3",
                    IssueEntity::issueType to "Task",
                    IssueEntity::project to "JiraReport SASS",
                    IssueEntity::priority to "Low",
                    IssueEntity::dynamicFields to mutableMapOf(
                        "field1" to "value3",
                        "field2" to "value2"
                    ),
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            )
        }

        return startDateFilter to endDateFilter
    }

}
