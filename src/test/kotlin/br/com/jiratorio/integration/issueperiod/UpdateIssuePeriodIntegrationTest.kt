package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import javax.servlet.http.HttpServletResponse.SC_NO_CONTENT

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateIssuePeriodIntegrationTest @Autowired constructor(
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
            issuePeriodFactory.create {
                it.startDate = "01/01/2019".toLocalDate()
                it.endDate = "31/01/2019".toLocalDate()
                it.boardId = board.id
            }
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
