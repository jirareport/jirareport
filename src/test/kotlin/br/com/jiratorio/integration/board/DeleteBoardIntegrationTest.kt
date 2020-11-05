package br.com.jiratorio.integration.board

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import br.com.jiratorio.repository.BoardRepository
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@IntegrationTest
class DeleteBoardIntegrationTest(
    private val boardFactory: BoardFactory,
    private val boardRepository: BoardRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `delete board`() {
        authenticator.withDefaultUser { boardFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }

        assertThat(boardRepository.count()).isZero()
    }

    @Test
    fun `delete other owner board`() {
        authenticator.withUser("other") {
            boardFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1")
            }
            then {
                spec(notFound())
            }
        }
    }

    @Test
    fun `delete board not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/9999")
            }
            then {
                spec(notFound())
            }
        }
    }
}
