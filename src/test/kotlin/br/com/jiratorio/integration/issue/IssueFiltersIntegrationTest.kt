package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.format.DateTimeFormatter

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IssueFiltersIntegrationTest @Autowired constructor(
    private val issueFactory: IssueFactory,
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory
) {

    @Test
    fun `test find filters`() {
        val startDateFilter = "01/01/2019".toLocalDate()
        val endDateFilter = "05/04/2019".toLocalDate()
        val dateRange = (startDateFilter..endDateFilter).toList()

        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()

            issueFactory.create {
                key = "JIRAT-1"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "P"
                system = "jirareport-api"
                epic = "v1"
                issueType = "Story"
                project = "JiraReport On Premise"
                priority = "Major"
                dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value1"
                )
            }
            issueFactory.create {
                key = "JIRAT-2"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "P"
                system = "jirareport-api"
                epic = "v1"
                issueType = "Story"
                project = "JiraReport On Premise"
                priority = "Major"
                dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value1"
                )
            }
            issueFactory.create {
                key = "JIRAT-3"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "M"
                system = "jirareport-api"
                epic = "v1"
                issueType = "Story"
                project = "JiraReport On Premise"
                priority = "Major"
                dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value2"
                )
            }
            issueFactory.create {
                key = "JIRAT-4"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "M"
                system = "jirareport-api"
                epic = "v2"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Medium"
                dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value1"
                )
            }
            issueFactory.create {
                key = "JIRAT-5"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "M"
                system = "jirareport-api"
                epic = "v2"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Medium"
                dynamicFields = mutableMapOf(
                    "field1" to "value2",
                    "field2" to "value1"
                )
            }
            issueFactory.create {
                key = "JIRAT-6"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "M"
                system = "jirareport-web"
                epic = "v3"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Medium"
                dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value2"
                )
            }
            issueFactory.create {
                key = "JIRAT-7"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "M"
                system = "jirareport-web"
                epic = "v3"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Expedite"
                dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value3"
                )
            }
            issueFactory.create {
                key = "JIRAT-8"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "G"
                system = "jirareport-web"
                epic = "v3"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Expedite"
                dynamicFields = mutableMapOf(
                    "field1" to "value2",
                    "field2" to "value1"
                )
            }
            issueFactory.create {
                key = "JIRAT-9"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "G"
                system = "jirareport-web"
                epic = "v3"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Blocker"
                dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value3"
                )
            }
            issueFactory.create {
                key = "JIRAT-10"
                board = defaultBoard
                endDate = dateRange.random().atTime(12, 0, 0)
                estimate = "P"
                system = "jirareport-web"
                epic = "v3"
                issueType = "Task"
                project = "JiraReport SASS"
                priority = "Low"
                dynamicFields = mutableMapOf(
                    "field1" to "value3",
                    "field2" to "value2"
                )
            }
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", startDateFilter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                param("endDate", endDateFilter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            }
            on {
                get("/boards/1/issues/filters")
            }
            then {
                statusCode(SC_OK)
                body("estimates", containsInAnyOrder("P", "M", "G"))
                body("keys", Matchers.hasSize<Int>(10))
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

}
