package br.com.jiratorio.integration.issue

import br.com.jiratorio.assert.response.assertThat
import br.com.jiratorio.base.Authenticator
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
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import javax.servlet.http.HttpServletResponse.SC_OK

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchIssueByIdIntegrationTest @Autowired constructor(
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

            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "field1"
            }
            dynamicFieldConfigFactory.create {
                it.board = board
                it.name = "field2"
            }

            val issue = issueFactory.create {
                it.board = board
                it.dynamicFields = mutableMapOf(
                    "field1" to "value1",
                    "field2" to "value1"
                )
                it.dueDateHistory = dueDateHistoryFactory.create(5)
            }

            leadTimeFactory.create {
                it.issue = issue
                it.leadTimeConfig = leadTimeConfigFactory.create {
                    it.board = board
                }
            }

            leadTimeFactory.create {
                it.issue = issue
                it.leadTimeConfig = leadTimeConfigFactory.create {
                    it.board = board
                }
            }

            impedimentHistoryFactory.create(5) {
                it.issueId = issue.id
            }
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

            hasChangelogSize(issue.changelog)
            hasDueDateHistorySize(issue.dueDateHistory)
            hasImpedimentHistorySize(issue.impedimentHistory)
            hasLeadTimesSize(issue.leadTimes)

            hasTouchTime(issue.touchTime / 60.0)
            hasWaitTime(issue.waitTime / 60.0)
            hasPctEfficiency(issue.pctEfficiency)
        }
    }

}
