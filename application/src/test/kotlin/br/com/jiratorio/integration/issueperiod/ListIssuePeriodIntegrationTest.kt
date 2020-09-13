package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.Authenticator
import br.com.jiratorio.assertion.response.assertThat
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodListResponse
import br.com.jiratorio.dsl.extractAs
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeFactory
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.restassured.specification.notFound
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@IntegrationTest
internal class ListIssuePeriodIntegrationTest(
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issueFactory: IssueFactory,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val leadTimeFactory: LeadTimeFactory,
) {

    @Test
    fun `test find by board`() {
        val period = authenticator.withDefaultUser {
            val board = boardFactory.create(
                modifyingFields = mapOf(
                    BoardEntity::estimateCF to "customfield_1234"
                )
            )

            val ltc1 = leadTimeConfigFactory.create(
                modifyingFields = mapOf(
                    LeadTimeConfigEntity::board to board,
                    LeadTimeConfigEntity::name to "Test Lead Time"
                )
            )

            val ltc2 = leadTimeConfigFactory.create(
                modifyingFields = mapOf(
                    LeadTimeConfigEntity::board to board,
                    LeadTimeConfigEntity::name to "Dev Lead Time"
                )
            )

            val ltc3 = leadTimeConfigFactory.create(
                modifyingFields = mapOf(
                    LeadTimeConfigEntity::board to board,
                    LeadTimeConfigEntity::name to "Delivery Lead Time"
                )
            )

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to board,
                    IssuePeriodEntity::startDate to "01/01/2019".toLocalDate(),
                    IssuePeriodEntity::endDate to "31/01/2019".toLocalDate(),
                    IssuePeriodEntity::name to board.issuePeriodNameFormat.format("01/01/2019".toLocalDate(), "31/01/2019".toLocalDate())
                )
            ).also { issuePeriod ->
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "P"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "M"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "G"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
            }

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to board,
                    IssuePeriodEntity::startDate to "01/02/2019".toLocalDate(),
                    IssuePeriodEntity::endDate to "28/02/2019".toLocalDate(),
                    IssuePeriodEntity::name to board.issuePeriodNameFormat.format("01/02/2019".toLocalDate(), "28/02/2019".toLocalDate())
                )
            ).also { issuePeriod ->
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "P"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "M"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "G"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
            }

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to board,
                    IssuePeriodEntity::startDate to "01/03/2019".toLocalDate(),
                    IssuePeriodEntity::endDate to "31/03/2019".toLocalDate(),
                    IssuePeriodEntity::name to board.issuePeriodNameFormat.format("01/03/2019".toLocalDate(), "31/03/2019".toLocalDate())
                )
            ).also { issuePeriod ->
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "P"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "M"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "G"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
            }

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to board,
                    IssuePeriodEntity::startDate to "01/04/2019".toLocalDate(),
                    IssuePeriodEntity::endDate to "30/04/2019".toLocalDate(),
                    IssuePeriodEntity::name to board.issuePeriodNameFormat.format("01/04/2019".toLocalDate(), "30/04/2019".toLocalDate())
                )
            ).also { issuePeriod ->
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "P"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "M"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "G"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
            }

            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriodEntity::board to board,
                    IssuePeriodEntity::startDate to "01/05/2019".toLocalDate(),
                    IssuePeriodEntity::endDate to "31/05/2019".toLocalDate(),
                    IssuePeriodEntity::name to board.issuePeriodNameFormat.format("01/05/2019".toLocalDate(), "31/05/2019".toLocalDate())
                )
            ).also { issuePeriod ->
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "P"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "M"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
                issueFactory.create(
                    modifyingFields = mapOf(
                        IssueEntity::board to board,
                        IssueEntity::issuePeriodId to issuePeriod.id,
                        IssueEntity::estimate to "G"
                    )
                ).also { issue ->
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc1,
                            LeadTimeEntity::leadTime to 5
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc2,
                            LeadTimeEntity::leadTime to 6
                        )
                    )
                    leadTimeFactory.create(
                        modifyingFields = mapOf(
                            LeadTimeEntity::issue to issue,
                            LeadTimeEntity::leadTimeConfig to ltc3,
                            LeadTimeEntity::leadTime to 7
                        )
                    )
                }
            }
        }

        val (
            periods,
            charts,
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

        periods.last()
            .assertThat {
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

            hasLeadTimeCompareChartSize("Test Lead Time", 5)
            hasLeadTimeCompareChartData("Test Lead Time", 5.0)

            hasLeadTimeCompareChartSize("Dev Lead Time", 5)
            hasLeadTimeCompareChartData("Dev Lead Time", 6.0)

            hasLeadTimeCompareChartSize("Delivery Lead Time", 5)
            hasLeadTimeCompareChartData("Delivery Lead Time", 7.0)

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
