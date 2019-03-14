package br.com.jiratorio.integration.board

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.repository.BoardRepository
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DeleteBoardIntegrationTest @Autowired constructor(
        private val boardFactory: BoardFactory,
        private val boardRepository: BoardRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun deleteBord() {
        authenticator.withDefaultUser { boardFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
         // @formatter:on

        assertThat(boardRepository.count()).isZero()
    }

    @Test
    fun deleteNonexistentBoard() {
        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
         // @formatter:on
    }

    @Test
    fun deleteBoardNotFound() {
        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/9999")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on
    }
}
