package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchIssueIntegrationTest @Autowired constructor(
    private val issueFactory: IssueFactory,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val authenticator: Authenticator
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

    @ParameterizedTest
    @MethodSource("filters")
    fun `test filter issue`(testParam: FilterParameter) {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-04")
                param(testParam.name, *testParam.value)
            }
            on {
                get("/boards/1/issues")
            }
            then {
                statusCode(SC_OK)
                body("issues", hasSize<Int>(testParam.expectedCount))
            }
        }
    }

    class FilterParameter(
        val name: String,
        val value: Array<String>,
        val expectedCount: Int
    ) {
        override fun toString(): String =
                "$name=[${value.joinToString()}] expected $expectedCount"
    }

    fun filters(): List<FilterParameter> {
        return listOf(
            FilterParameter(
                name = "estimates",
                value = arrayOf("P"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "estimates",
                value = arrayOf("M"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "estimates",
                value = arrayOf("G"),
                expectedCount = 2
            ),
            FilterParameter(
                name = "estimates",
                value = arrayOf("P", "G"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "systems",
                value = arrayOf("jirareport-api"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "systems",
                value = arrayOf("jirareport-web"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "epics",
                value = arrayOf("v1"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "epics",
                value = arrayOf("v2"),
                expectedCount = 2
            ),
            FilterParameter(
                name = "epics",
                value = arrayOf("v3"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "epics",
                value = arrayOf("v1", "v3"),
                expectedCount = 8
            ),
            FilterParameter(
                name = "issueTypes",
                value = arrayOf("Story"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "issueTypes",
                value = arrayOf("Task"),
                expectedCount = 7
            ),
            FilterParameter(
                name = "issueTypes",
                value = arrayOf("Story", "Task"),
                expectedCount = 10
            ),
            FilterParameter(
                name = "projects",
                value = arrayOf("JiraReport On Premise"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "projects",
                value = arrayOf("JiraReport SASS"),
                expectedCount = 7
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Major"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Medium"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Expedite"),
                expectedCount = 2
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Blocker"),
                expectedCount = 1
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Low"),
                expectedCount = 1
            ),
            FilterParameter(
                name = "priorities",
                value = arrayOf("Low", "Major", "Medium"),
                expectedCount = 7
            ),
            FilterParameter(
                name = "field1",
                value = arrayOf("value1"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "field1",
                value = arrayOf("value2"),
                expectedCount = 2
            ),
            FilterParameter(
                name = "field1",
                value = arrayOf("value3"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "field1",
                value = arrayOf("value2", "value3"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "field2",
                value = arrayOf("value1"),
                expectedCount = 5
            ),
            FilterParameter(
                name = "field2",
                value = arrayOf("value2"),
                expectedCount = 3
            ),
            FilterParameter(
                name = "field2",
                value = arrayOf("value3"),
                expectedCount = 2
            ),
            FilterParameter(
                name = "field2",
                value = arrayOf("value1", "value2"),
                expectedCount = 8
            )
        )
    }

    fun createIssues() {
        val startDateFilter = "01/01/2019".toLocalDate()
        val endDateFilter = "05/04/2019".toLocalDate()
        val dateRange = (startDateFilter..endDateFilter).toList()

        authenticator.withDefaultUser {
            val board = boardFactory.create()
            val issuePeriod = issuePeriodFactory.create {
                it.boardId = board.id
            }

            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "field1"
            }
            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "field2"
            }
            issueFactory.create {
                it.key = "JIRAT-1"
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
                it.board = board
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
    }

}
