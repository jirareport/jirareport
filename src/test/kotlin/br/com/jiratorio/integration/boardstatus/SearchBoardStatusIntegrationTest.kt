package br.com.jiratorio.integration.boardstatus

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.annotation.LoadStubs
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test

@IntegrationTest
class SearchBoardStatusIntegrationTest(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    @LoadStubs(["board-statuses"])
    fun `test find by board id`() {
        boardFactory.create(
            modifyingFields = mapOf(
                BoardEntity::externalId to 10552
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
