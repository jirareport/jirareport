package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.IssueFactory
import br.com.jiratorio.factory.domain.entity.IssuePeriodFactory
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.servlet.http.HttpServletResponse.SC_OK

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class FindIssuePeriodIntegrationTest @Autowired constructor(
    private val authenticator: Authenticator,
    private val issuePeriodFactory: IssuePeriodFactory,
    private val issueFactory: IssueFactory,
    private val boardFactory: BoardFactory
) {

    @Test
    fun `test find by id`() {
        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()
            val mutableIssues = issueFactory.create(20) {
                board = defaultBoard
            }.toMutableList()

            issuePeriodFactory.create {
                boardId = defaultBoard.id
                issues = mutableIssues
            }
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/issue-periods/1")
            }
            then {
                statusCode(SC_OK)
            }
        }
    }

}
