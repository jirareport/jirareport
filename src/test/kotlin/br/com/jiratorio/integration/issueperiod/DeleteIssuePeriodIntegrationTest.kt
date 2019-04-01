package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.entity.IssuePeriodFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.servlet.http.HttpServletResponse.SC_NO_CONTENT

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DeleteIssuePeriodIntegrationTest @Autowired constructor(
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
