package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.assert.response.assertThat
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.domain.response.issue.IssueResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import br.com.jiratorio.dsl.extractAs
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.repository.IssueRepository
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse.SC_OK

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class FindIssuePeriodIntegrationTest @Autowired constructor(
    private val authenticator: Authenticator,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issueFactory: IssueFactory,
    private val boardFactory: BoardFactory,
    private val issueRepository: IssueRepository
) {

    @Test
    fun `test find by id`() {
        val issuePeriod = authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()

            val issuePeriod = issuePeriodFactory.create {
                it.boardId = defaultBoard.id
            }

            issueFactory.create(20) {
                it.board = defaultBoard
                it.issuePeriodId = issuePeriod.id
            }.toMutableList()

            issuePeriod
        }

        val (
            detail: IssuePeriodDetailResponse,
            issues: List<IssueResponse>
        ) = restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods/1")
            }
            then {
                statusCode(SC_OK)
            }
        } extractAs IssuePeriodByIdResponse::class

        detail.assertThat {
            hasDates(issuePeriod.dates)
            hasLeadTime(issuePeriod.leadTime)
            hasThroughput(issuePeriod.throughput)
            hasLeadTimeByEstimate(issuePeriod.leadTimeByEstimate)
            hasThroughputByEstimate(issuePeriod.throughputByEstimate)
            hasLeadTimeBySystem(issuePeriod.leadTimeBySystem)
            hasThroughputBySystem(issuePeriod.throughputBySystem)
            hasLeadTimeByType(issuePeriod.leadTimeByType)
            hasThroughputByType(issuePeriod.throughputByType)
            hasLeadTimeByProject(issuePeriod.leadTimeByProject)
            hasThroughputByProject(issuePeriod.throughputByProject)
            hasLeadTimeByPriority(issuePeriod.leadTimeByPriority)
            hasThroughputByPriority(issuePeriod.throughputByPriority)
            hasColumnTimeAvg(issuePeriod.columnTimeAvg)
            hasLeadTimeCompareChart(issuePeriod.leadTimeCompareChart)
            hasDynamicCharts(issuePeriod.dynamicCharts)
        }

        val issueResponse = issues.first()
        val issue = issueRepository.findByIdOrNull(issueResponse.id) ?: throw ResourceNotFound()

        issueResponse.assertThat {
            hasId(issue.id)
            hasKey(issue.key)
            hasCreator(issue.creator)
            hasSummary(issue.summary)
            hasIssueType(issue.issueType)
            hasEstimate(issue.estimate)
            hasProject(issue.project)
            hasEpic(issue.epic)
            hasSystem(issue.system)
            hasPriority(issue.priority)
            hasLeadTime(issue.leadTime)
            hasStartDate(issue.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            hasEndDate(issue.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            hasCreated(issue.created.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            hasDeviationOfEstimate(issue.deviationOfEstimate)
            hasChangeEstimateCount(issue.dueDateHistory?.size)
            hasImpedimentTime(issue.impedimentTime)
            hasDynamicFields(issue.dynamicFields)
        }

    }

}
