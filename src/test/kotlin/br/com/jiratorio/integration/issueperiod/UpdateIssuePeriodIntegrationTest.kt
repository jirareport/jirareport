package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.config.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletResponse.SC_NO_CONTENT

@IntegrationTest
internal class UpdateIssuePeriodIntegrationTest(
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issuePeriodRepository: IssuePeriodRepository
) {

    @Test
    @LoadStubs(["issues/basic-issues"])
    fun `test update`() {
        authenticator.withDefaultUser {
            val board = boardFactory.create(boardFactory::withBasicConfigurationBuilder)
            issuePeriodFactory.create(
                modifyingFields = mapOf(
                    IssuePeriod::startDate to "01/01/2019".toLocalDate(),
                    IssuePeriod::endDate to "31/01/2019".toLocalDate(),
                    IssuePeriod::board to board
                )
            )
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                put("/boards/1/issue-periods/1")
            }
            then {
                statusCode(SC_NO_CONTENT)
            }
        }

        assertThat(issuePeriodRepository.findByIdOrNull(1L))
            .isNull()

        assertThat(issuePeriodRepository.findByIdOrNull(2L))
            .isNotNull()
    }

    @Test
    fun `test update with board not found`() {
        authenticator.withDefaultUser {
            issuePeriodFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                put("/boards/2/issue-periods/1")
            }
            then {
                spec(notFound())
            }
        }

        assertThat(issuePeriodRepository.findByIdOrNull(1L))
            .isNotNull()
    }

    @Test
    fun `test update with period not found`() {
        authenticator.withDefaultUser {
            issuePeriodFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                put("/boards/1/issue-periods/2")
            }
            then {
                spec(notFound())
            }
        }

        assertThat(issuePeriodRepository.findByIdOrNull(1L))
            .isNotNull()
    }
}
