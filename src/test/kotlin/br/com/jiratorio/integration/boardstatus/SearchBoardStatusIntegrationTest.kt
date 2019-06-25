package br.com.jiratorio.integration.boardstatus

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SearchBoardStatusIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    @LoadStubs(["board-statuses"])
    fun `test find by board id`() {
        boardFactory.create {
            it.externalId = 10552
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/statuses")
            }
            then {
                statusCode(SC_OK)
                body(
                    "$",
                    containsInAnyOrder(
                        "Open",
                        "In Progress",
                        "Resolved",
                        "Reopened",
                        "Closed",
                        "Waiting for Feedback",
                        "Investigating",
                        "Waiting for Review"
                    )
                )
            }
        }
    }

    @Test
    @LoadStubs(["board-statuses"])
    fun `test find by board id not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/statuses")
            }
            then {
                spec(notFound())
            }
        }
    }
}
