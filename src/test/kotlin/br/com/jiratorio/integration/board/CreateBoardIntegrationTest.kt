package br.com.jiratorio.integration.board

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.assertion.BoardAssert.Companion.assertThat
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.testlibrary.factory.domain.request.CreateBoardRequestFactory
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.repository.BoardRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test

@IntegrationTest
class CreateBoardIntegrationTest(
    private val boardRepository: BoardRepository,
    private val createBoardRequestFactory: CreateBoardRequestFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `create board`() {
        val request = createBoardRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards")
            }
            then {
                statusCode(HttpStatus.SC_CREATED)
                header("location", containsString("/boards/1"))
            }
        }

        val board = boardRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        assertThat(board)
            .hasName(request.name)
            .hasExternalId(request.externalId)
            .hasOwner("default_user")
    }

    @Test
    fun `fail in validations`() {
        val createBoardRequest = object {
            val name: String = ""
            val externalId: Long = 0
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(createBoardRequest)
            }
            on {
                post("/boards")
            }
            then {
                statusCode(HttpStatus.SC_BAD_REQUEST)
                body("externalId", contains("must be greater than or equal to 1"))
                body("name", contains("must not be blank"))
            }
        }
    }
}
