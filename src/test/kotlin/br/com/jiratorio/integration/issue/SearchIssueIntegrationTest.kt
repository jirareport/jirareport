package br.com.jiratorio.integration.issue

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.IssuePeriod
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchIssueIntegrationTest(
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
            val issuePeriod = issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board
                )
            )

            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfig::board to board,
                    DynamicFieldConfig::name to "field1"
                )
            )
            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfig::board to board,
                    DynamicFieldConfig::name to "field2"
                )
            )
            issueFactory.create(
                modifyingFields = mapOf(
                    Issue::key to "JIRAT-1",
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
                    Issue::board to board,
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
    }

}
