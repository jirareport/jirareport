package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletResponse.SC_NO_CONTENT

@IntegrationTest
class DeleteIssuePeriodIntegrationTest(
    private val authenticator: Authenticator,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issuePeriodRepository: IssuePeriodRepository
) {

    @Test
    fun `test delete by board and id`() {
        authenticator.withDefaultUser { issuePeriodFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/issue-periods/1")
            }
            then {
                statusCode(SC_NO_CONTENT)
            }
        }
        assertThat(issuePeriodRepository.count())
            .isZero()
    }

    @Test
    fun `test delete by board not found and id`() {
        authenticator.withDefaultUser { issuePeriodFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/2/issue-periods/1")
            }
            then {
                spec(notFound())
            }
        }
        assertThat(issuePeriodRepository.count())
            .isOne()
    }

    @Test
    fun `test delete by board and id not found`() {
        authenticator.withDefaultUser { issuePeriodFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/issue-periods/2")
            }
            then {
                spec(notFound())
            }
        }
        assertThat(issuePeriodRepository.count())
            .isOne()
    }
}
