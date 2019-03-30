package br.com.jiratorio.integration.board

import br.com.jiratorio.assert.BoardAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.CreateBoardRequestFactory
import br.com.jiratorio.repository.BoardRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateBoardIntegrationTest @Autowired constructor(
    private val boardRepository: BoardRepository,
    private val createBoardRequestFactory: CreateBoardRequestFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `create board`() {
        val request = createBoardRequestFactory.build()

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

        val board = boardRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        BoardAssert(board).assertThat {
            hasName(request.name)
            hasExternalId(request.externalId)
            hasOwner("default_user")
        }
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
                body("errors.find { it.field == 'externalId' }.messages", contains("must be greater than or equal to 1"))
                body("errors.find { it.field == 'name' }.messages", contains("must not be blank"))
            }
        }
    }
}
