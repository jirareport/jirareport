package br.com.jiratorio.integration.issue

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.testlibrary.extension.toLocalDate
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.IssueFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@IntegrationTest
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
                    IssuePeriodEntity::board to board
                )
            )

            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board,
                    DynamicFieldConfigEntity::name to "field1"
                )
            )
            dynamicFieldConfigFactory.create(
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board,
                    DynamicFieldConfigEntity::name to "field2"
                )
            )
            issueFactory.create(
                modifyingFields = mapOf(
                    IssueEntity::key to "JIRAT-1",
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
                    IssueEntity::board to board,
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
    }

}
