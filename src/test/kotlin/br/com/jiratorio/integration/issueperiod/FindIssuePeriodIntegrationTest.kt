package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.assertion.response.IssuePeriodDetailResponseAssert
import br.com.jiratorio.testlibrary.assertion.response.IssueResponseAssert
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.domain.response.issue.IssueResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse
import br.com.jiratorio.testlibrary.dsl.extractAs
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.IssueFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.repository.IssueRepository
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse.SC_OK

@IntegrationTest
class FindIssuePeriodIntegrationTest(
    private val authenticator: Authenticator,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issueFactory: IssueFactory,
    private val boardFactory: BoardFactory,
    private val issueRepository: IssueRepository,
) {

    @Test
    fun `test find by id`() {
        val issuePeriod = authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()

            val issuePeriod = issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to defaultBoard
                )
            )

            issueFactory.create(
                quantity = 20,
                modifyingFields = mapOf(
                    IssueEntity::board to defaultBoard,
                    IssueEntity::issuePeriodId to issuePeriod.id
                )
            ).toMutableList()

            issuePeriod
        }

        val (
            detail: IssuePeriodDetailResponse,
            issues: List<IssueResponse>,
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

        IssuePeriodDetailResponseAssert.assertThat(detail)
            .hasName(issuePeriod.name)
            .hasLeadTime(issuePeriod.leadTime)
            .hasThroughput(issuePeriod.throughput)
            .hasLeadTimeByEstimate(issuePeriod.leadTimeByEstimate)
            .hasThroughputByEstimate(issuePeriod.throughputByEstimate)
            .hasLeadTimeBySystem(issuePeriod.leadTimeBySystem)
            .hasThroughputBySystem(issuePeriod.throughputBySystem)
            .hasLeadTimeByType(issuePeriod.leadTimeByType)
            .hasThroughputByType(issuePeriod.throughputByType)
            .hasLeadTimeByProject(issuePeriod.leadTimeByProject)
            .hasThroughputByProject(issuePeriod.throughputByProject)
            .hasLeadTimeByPriority(issuePeriod.leadTimeByPriority)
            .hasThroughputByPriority(issuePeriod.throughputByPriority)
            .hasColumnTimeAverages(issuePeriod.columnTimeAverages)
            .hasLeadTimeCompareChart(issuePeriod.leadTimeCompareChart)
            .hasDynamicCharts(issuePeriod.dynamicCharts)

        val issueResponse = issues.first()
        val issue = issueRepository.findByIdOrNull(issueResponse.id) ?: throw ResourceNotFound()

        IssueResponseAssert.assertThat(issueResponse)
            .hasId(issue.id)
            .hasKey(issue.key)
            .hasCreator(issue.creator)
            .hasSummary(issue.summary)
            .hasIssueType(issue.issueType)
            .hasEstimate(issue.estimate)
            .hasProject(issue.project)
            .hasEpic(issue.epic)
            .hasSystem(issue.system)
            .hasPriority(issue.priority)
            .hasLeadTime(issue.leadTime)
            .hasStartDate(issue.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            .hasEndDate(issue.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            .hasCreated(issue.created.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            .hasDeviationOfEstimate(issue.deviationOfEstimate)
            .hasChangeEstimateCount(issue.dueDateHistory?.size ?: 0)
            .hasImpedimentTime(issue.impedimentTime)
            .hasDynamicFields(issue.dynamicFields)
    }

}
