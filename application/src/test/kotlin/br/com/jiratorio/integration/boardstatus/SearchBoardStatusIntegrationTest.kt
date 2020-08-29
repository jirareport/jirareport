package br.com.jiratorio.integration.boardstatus

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.config.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test

@IntegrationTest
internal class SearchBoardStatusIntegrationTest(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    @LoadStubs(["board-statuses"])
    fun `test find by board id`() {
        boardFactory.create(
            modifyingFields = mapOf(
                Board::externalId to 10552
            )
        )

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
