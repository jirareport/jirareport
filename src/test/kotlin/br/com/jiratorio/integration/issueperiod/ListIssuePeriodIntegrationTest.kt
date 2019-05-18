package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.assert.response.IssuePeriodResponseAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByBoardResponse
import br.com.jiratorio.dsl.extractAs
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.decimal.format
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ListIssuePeriodIntegrationTest @Autowired constructor(
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val issuePeriodFactory: IssuePeriodFactory
) {

    @Test
    fun `test find by board`() {
        val period = authenticator.withDefaultUser {
            val board = boardFactory.create()

            issuePeriodFactory.create {
                it.boardId = board.id
                it.startDate = "01/01/2019".toLocalDate()
                it.endDate = "31/01/2019".toLocalDate()
            }
            issuePeriodFactory.create {
                it.boardId = board.id
                it.startDate = "01/02/2019".toLocalDate()
                it.endDate = "28/02/2019".toLocalDate()
            }
            issuePeriodFactory.create {
                it.boardId = board.id
                it.startDate = "01/03/2019".toLocalDate()
                it.endDate = "31/03/2019".toLocalDate()
            }
            issuePeriodFactory.create {
                it.boardId = board.id
                it.startDate = "01/04/2019".toLocalDate()
                it.endDate = "30/04/2019".toLocalDate()
            }
            issuePeriodFactory.create {
                it.boardId = board.id
                it.startDate = "01/05/2019".toLocalDate()
                it.endDate = "31/05/2019".toLocalDate()
            }
        }

        val response = restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods")
            }
            then {
                statusCode(SC_OK)
            }
        } extractAs IssuePeriodByBoardResponse::class

        IssuePeriodResponseAssert(response.periods.last()).assertThat {
            hasId(period.id)
            hasDates(period.dates)
            hasLeadTime(period.leadTime)
            hasAvgPctEfficiency(period.avgPctEfficiency)
            hasWipAvg(period.wipAvg)
            hasJql(period.jql)
            hasThroughput(period.throughput)
        }

        assertThat(response.charts.throughputByEstimate.labels)
            .hasSize(5)
        assertThat(response.charts.throughputByEstimate.datasources["P"])
            .hasSize(5)
        assertThat(response.charts.throughputByEstimate.datasources["M"])
            .hasSize(5)
        assertThat(response.charts.throughputByEstimate.datasources["G"])
            .hasSize(5)
        assertThat(response.charts.throughputByEstimate.datasources["M"])
            .hasSize(5)
        assertThat(response.charts.throughputByEstimate.datasources["G"])
            .hasSize(5)

        assertThat(response.charts.leadTimeCompareChart.labels)
            .hasSize(5)
        assertThat(response.charts.leadTimeCompareChart.datasources["Test Lead Time"])
            .hasSize(5)
        assertThat(response.charts.leadTimeCompareChart.datasources["Test Lead Time"]?.last())
            .isEqualTo(period.leadTimeCompareChart?.data!!["Test Lead Time"])

        assertThat(response.charts.leadTimeCompareChart.datasources["Dev Lead Time"])
            .hasSize(5)
        assertThat(response.charts.leadTimeCompareChart.datasources["Dev Lead Time"]?.last())
            .isEqualTo(period.leadTimeCompareChart?.data!!["Dev Lead Time"])

        assertThat(response.charts.leadTimeCompareChart.datasources["Delivery Lead Time"])
            .hasSize(5)
        assertThat(response.charts.leadTimeCompareChart.datasources["Delivery Lead Time"]?.last())
            .isEqualTo(period.leadTimeCompareChart?.data!!["Delivery Lead Time"])

        assertThat(response.charts.leadTime.data[period.dates])
            .isEqualTo(period.leadTime.format())
        assertThat(response.charts.throughput.data[period.dates])
            .isEqualTo(period.throughput)
    }

    @Test
    fun `test search by board not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods")
            }
            then {
                spec(notFound())
            }
        }
    }

    @Test
    fun `test search by board empty result`() {
        authenticator.withDefaultUser { boardFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods")
            }
            then {
                statusCode(SC_OK)
                body("periods", Matchers.empty<Any>())
            }
        }
    }
}
