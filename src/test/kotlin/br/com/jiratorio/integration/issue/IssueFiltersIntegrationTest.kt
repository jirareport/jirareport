package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IssueFiltersIntegrationTest @Autowired constructor(
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
            val issuePeriod = issuePeriodFactory.create {
                it.boardId = defaultBoard.id
            }

            issueFactory.create {
                it.key = "JIRAT-1"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "P"
                it.system = "jirareport-api"
                it.epic = "v1"
                it.issueType = "Story"
                it.project = "JiraReport On Premise"
                it.priority = "Major"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value1"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-2"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "P"
                it.system = "jirareport-api"
                it.epic = "v1"
                it.issueType = "Story"
                it.project = "JiraReport On Premise"
                it.priority = "Major"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value1"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-3"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "M"
                it.system = "jirareport-api"
                it.epic = "v1"
                it.issueType = "Story"
                it.project = "JiraReport On Premise"
                it.priority = "Major"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value2"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-4"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "M"
                it.system = "jirareport-api"
                it.epic = "v2"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Medium"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value1"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-5"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "M"
                it.system = "jirareport-api"
                it.epic = "v2"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Medium"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value2",
                    "field2" to "value1"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-6"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "M"
                it.system = "jirareport-web"
                it.epic = "v3"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Medium"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value2"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-7"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "M"
                it.system = "jirareport-web"
                it.epic = "v3"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Expedite"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value3"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-8"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "G"
                it.system = "jirareport-web"
                it.epic = "v3"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Expedite"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value2",
                    "field2" to "value1"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-9"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "G"
                it.system = "jirareport-web"
                it.epic = "v3"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Blocker"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value3"
                )
                it.issuePeriodId = issuePeriod.id
            }
            issueFactory.create {
                it.key = "JIRAT-10"
                it.board = defaultBoard
                it.endDate = dateRange.random().atTime(12, 0, 0)
                it.estimate = "P"
                it.system = "jirareport-web"
                it.epic = "v3"
                it.issueType = "Task"
                it.project = "JiraReport SASS"
                it.priority = "Low"
                it.dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value2"
                )
                it.issuePeriodId = issuePeriod.id
            }
        }

        return startDateFilter to endDateFilter
    }

}
