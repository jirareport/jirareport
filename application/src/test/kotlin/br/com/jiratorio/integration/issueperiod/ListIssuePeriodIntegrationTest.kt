package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.assertion.response.assertThat
import br.com.jiratorio.Authenticator
import br.com.jiratorio.restassured.specification.notFound
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodListResponse
import br.com.jiratorio.dsl.extractAs
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@IntegrationTest
internal class ListIssuePeriodIntegrationTest(
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val issuePeriodFactory: IssuePeriodFactory
) {

    @Test
    fun `test find by board`() {
        val period = authenticator.withDefaultUser {
            val board = boardFactory.create()

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board,
                    IssuePeriod::startDate to "01/01/2019".toLocalDate(),
                    IssuePeriod::endDate to "31/01/2019".toLocalDate()
                )
            )
            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board,
                    IssuePeriod::startDate to "01/02/2019".toLocalDate(),
                    IssuePeriod::endDate to "28/02/2019".toLocalDate()
                )
            )
            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board,
                    IssuePeriod::startDate to "01/03/2019".toLocalDate(),
                    IssuePeriod::endDate to "31/03/2019".toLocalDate()
                )
            )
            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board,
                    IssuePeriod::startDate to "01/04/2019".toLocalDate(),
                    IssuePeriod::endDate to "30/04/2019".toLocalDate()
                )
            )
            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::board to board,
                    IssuePeriod::startDate to "01/05/2019".toLocalDate(),
                    IssuePeriod::endDate to "31/05/2019".toLocalDate()
                )
            )
        }

        val (
            periods,
            charts
        ) = restAssured {
            given {
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-31")
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods")
            }
            then {
                statusCode(SC_OK)
            }
        } extractAs IssuePeriodListResponse::class

        periods.last().assertThat {
            hasId(period.id)
            hasName(period.name)
            hasLeadTime(period.leadTime)
            hasAvgPctEfficiency(period.avgPctEfficiency)
            hasWipAvg(period.wipAvg)
            hasJql(period.jql)
            hasThroughput(period.throughput)
        }

        charts.assertThat {
            hasThroughputByEstimateLabelsSize(5)

            hasThroughputByEstimateSize("P", 5)
            hasThroughputByEstimateSize("M", 5)
            hasThroughputByEstimateSize("G", 5)

            hasLeadTimeCompareChartLabelsSize(5)

            period.leadTimeCompareChart?.let { leadTimeCompareChart ->
                hasLeadTimeCompareChartSize("Test Lead Time", 5)
                hasLeadTimeCompareChartData("Test Lead Time", leadTimeCompareChart.data["Test Lead Time"])

                hasLeadTimeCompareChartSize("Dev Lead Time", 5)
                hasLeadTimeCompareChartData("Dev Lead Time", leadTimeCompareChart.data["Dev Lead Time"])

                hasLeadTimeCompareChartSize("Delivery Lead Time", 5)
                hasLeadTimeCompareChartData("Delivery Lead Time", leadTimeCompareChart.data["Delivery Lead Time"])
            } ?: Assertions.fail("period.leadTimeCompareChart can't be null")

            hasLeadTime(period.name, period.leadTime)
            hasThroughput(period.name, period.throughput)
        }
    }

    @Test
    fun `test search by board not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-31")
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
                param("startDate", "2019-01-01")
                param("endDate", "2019-05-31")
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
