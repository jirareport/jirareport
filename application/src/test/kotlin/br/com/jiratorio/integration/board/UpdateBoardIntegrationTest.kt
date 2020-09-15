package br.com.jiratorio.integration.board

import br.com.jiratorio.Authenticator
import br.com.jiratorio.assertion.BoardAssert.Companion.assertThat
import br.com.jiratorio.restassured.specification.notFound
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.request.UpdateBoardRequestFactory
import br.com.jiratorio.repository.BoardRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

@IntegrationTest
internal class UpdateBoardIntegrationTest(
    private val boardFactory: BoardFactory,
    private val updateBoardRequestFactory: UpdateBoardRequestFactory,
    private val boardRepository: BoardRepository,
    private val authenticator: Authenticator,
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

        val board = boardRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        assertThat(board)
            .hasName(request.name)
            .hasStartColumn(request.startColumn)
            .hasEndColumn(request.endColumn)
            .hasFluxColumn(request.fluxColumn?.map { it.toUpperCase() })
            .hasTouchingColumns(request.touchingColumns)
            .hasWaitingColumns(request.waitingColumns)
            .hasIgnoreIssueType(request.ignoreIssueType)
            .hasEpicCF(request.epicCF)
            .hasEstimateCF(request.estimateCF)
            .hasSystemCF(request.systemCF)
            .hasProjectCF(request.projectCF)
            .hasIgnoreWeekend(request.ignoreWeekend)
            .hasImpedimentColumns(request.impedimentColumns?.map { it.toUpperCase() })
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
