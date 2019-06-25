package br.com.jiratorio.integration.board

import br.com.jiratorio.assert.BoardAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.request.UpdateBoardRequestFactory
import br.com.jiratorio.repository.BoardRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateBoardIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val updateBoardRequestFactory: UpdateBoardRequestFactory,
    private val boardRepository: BoardRepository,
    private val authenticator: Authenticator
) {

    @Test
    @Throws(Exception::class)
    fun `update board`() {
        authenticator.withDefaultUser { boardFactory.create() }
        val request = updateBoardRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                put("/boards/1")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }

        val board = boardRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        BoardAssert(board).assertThat {
            hasName(request.name)
            hasStartColumn(request.startColumn)
            hasEndColumn(request.endColumn)
            hasFluxColumn(request.fluxColumn)
            hasTouchingColumns(request.touchingColumns)
            hasWaitingColumns(request.waitingColumns)
            hasIgnoreIssueType(request.ignoreIssueType)
            hasEpicCF(request.epicCF)
            hasEstimateCF(request.estimateCF)
            hasSystemCF(request.systemCF)
            hasProjectCF(request.projectCF)
            hasIgnoreWeekend(request.ignoreWeekend)
            hasImpedimentColumns(request.impedimentColumns)
        }
    }

    @Test
    fun `update not found board`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(UpdateBoardRequest("name"))
            }
            on {
                put("/boards/999")
            }
            then {
                spec(notFound())
            }
        }
    }
}
