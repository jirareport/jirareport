package br.com.jiratorio.integration.issue

import br.com.jiratorio.assert.response.assertThat
import br.com.jiratorio.Authenticator
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.dsl.extractAs
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.factory.domain.entity.ImpedimentHistoryFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeFactory
import br.com.jiratorio.factory.domain.request.DueDateHistoryFactory
import br.com.jiratorio.repository.IssueRepository
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletResponse.SC_OK

@IntegrationTest
class SearchIssueByIdIntegrationTest(
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val issueFactory: IssueFactory,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val leadTimeFactory: LeadTimeFactory,
    private val dueDateHistoryFactory: DueDateHistoryFactory,
    private val impedimentHistoryFactory: ImpedimentHistoryFactory,
    private val issueRepository: IssueRepository
) {

    @Test
    fun `test find by id`() {
        authenticator.withDefaultUser {
            val board = boardFactory.create()

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

            val issue = issueFactory.create(
                modifyingFields = mapOf(
                    Issue::board to board,
                    Issue::dynamicFields to mutableMapOf(
                        "field1" to "value1",
                        "field2" to "value1"
                    ),
                    Issue::dueDateHistory to dueDateHistoryFactory.create(5)
                )
            )

            leadTimeFactory.create(
                modifyingFields = mapOf(
                    LeadTime::issue to issue,
                    LeadTime::leadTimeConfig to leadTimeConfigFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeConfig::board to board
                        )
                    )
                )
            )

            leadTimeFactory.create(
                modifyingFields = mapOf(
                    LeadTime::issue to issue,
                    LeadTime::leadTimeConfig to leadTimeConfigFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeConfig::board to board
                        )
                    )
                )
            )

            impedimentHistoryFactory.create(
                quantity = 5,
                modifyingFields = mapOf(
                    ImpedimentHistory::issueId to issue.id
                )
            )
        }

        val issueDetailResponse = restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issues/1")
            }
            then {
                statusCode(SC_OK)
            }
        } extractAs IssueDetailResponse::class

        val issue = issueRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        issueDetailResponse.assertThat {
            hasId(issue.id)
            hasKey(issue.key)

            hasChangelogSize(issue.columnChangelog)
            hasDueDateHistorySize(issue.dueDateHistory)
            hasImpedimentHistorySize(issue.impedimentHistory)
            hasLeadTimesSize(issue.leadTimes)

            hasTouchTime(issue.touchTime / 60.0)
            hasWaitTime(issue.waitTime / 60.0)
            hasPctEfficiency(issue.pctEfficiency)
        }
    }

}
