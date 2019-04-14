package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchIssueIntegrationTest @Autowired constructor(
    private val issueFactory: IssueFactory,
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory
) {

    @BeforeEach
    fun setUp() {
        createIssues()
    }

    @Test
    fun `test find all`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-04")
            }
            on {
                get("/boards/1/issues")
            }
            then {
                statusCode(SC_OK)
                body("issues", hasSize<Int>(10))
            }
        }
    }

    @Test
    fun `test filter by multiple value`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-04")
                param("estimates", "P", "M")
            }
            on {
                get("/boards/1/issues")
            }
            then {
                statusCode(SC_OK)
                body("issues", hasSize<Int>(8))
            }
        }
    }

    @ParameterizedTest
    @MethodSource("filters")
    fun `test filter issue`(testParam: Pair<Pair<String, String>, Int>) {
        val (param, expected) = testParam

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-04")
                param(param.first, param.second)
            }
            on {
                get("/boards/1/issues")
            }
            then {
                statusCode(SC_OK)
                body("issues", hasSize<Int>(expected))
            }
        }
    }

    fun filters(): List<Pair<Pair<String, String>, Int>> {
        return listOf(
            "estimates" to "P" to 3,
            "estimates" to "M" to 5,
            "estimates" to "G" to 2,
            "systems" to "jirareport-api" to 5,
            "systems" to "jirareport-web" to 5,
            "epics" to "v1" to 3,
            "epics" to "v2" to 2,
            "epics" to "v3" to 5,
            "issueTypes" to "Story" to 3,
            "issueTypes" to "Task" to 7,
            "projects" to "JiraReport On Premise" to 3,
            "projects" to "JiraReport SASS" to 7,
            "priorities" to "Major" to 3,
            "priorities" to "Medium" to 3,
            "priorities" to "Expedite" to 2,
            "priorities" to "Blocker" to 1,
            "priorities" to "Low" to 1
        )
    }

    fun createIssues() {
        val startDateFilter = "01/01/2019".toLocalDate()
        val endDateFilter = "05/04/2019".toLocalDate()
        val dateRange = (startDateFilter..endDateFilter).toList()

        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create {
                dynamicFields = mutableListOf(
                    DynamicFieldConfig("field1", "field1"),
                    DynamicFieldConfig("field2", "field1")
                )
            }

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
    }

}
