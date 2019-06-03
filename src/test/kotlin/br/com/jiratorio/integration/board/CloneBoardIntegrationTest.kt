package br.com.jiratorio.integration.board

import br.com.jiratorio.assert.BoardAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.repository.BoardRepository
import org.apache.http.HttpStatus.SC_CREATED
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloneBoardIntegrationTest @Autowired constructor(
    private val boardRepository: BoardRepository,
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory
) {

    @Test
    fun `test clone a simple board`() {
        val boardToClone = authenticator.withDefaultUser {
            boardFactory.create(boardFactory::withCompleteConfigurationBuilder)
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                post("/boards?boardIdToClone=1")
            }
            then {
                statusCode(SC_CREATED)
                header("location", Matchers.containsString("/boards/2"))
            }
        }

        val board = (boardRepository.findByIdOrNull(2L)
            ?: throw ResourceNotFound())

        BoardAssert(board).assertThat {
            hasExternalId(boardToClone.externalId)
            hasName(boardToClone.name)
        }

    }

}
