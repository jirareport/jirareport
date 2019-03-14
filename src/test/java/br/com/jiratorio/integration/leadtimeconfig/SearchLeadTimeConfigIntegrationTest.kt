package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.resolver.SpecificationResolver
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory
import br.com.jiratorio.matcher.IdMatcher
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class, SpecificationResolver::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchLeadTimeConfigIntegrationTest @Autowired constructor(
        private val leadTimeConfigFactory: LeadTimeConfigFactory,
        private val boardFactory: BoardFactory,
        private val authenticator: Authenticator
) {

    @Test
    fun findAllLeadTimeConfig() {
        authenticator.withDefaultUser {
            val board = boardFactory.create()
            leadTimeConfigFactory.create(10) {
                it.board = board
            }
        }

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .`when`()
                    .get("/boards/1/lead-time-configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("$", Matchers.hasSize<Int>(10))
                    .body("findAll { it.boardId == 1 }", Matchers.hasSize<Int>(10))
        // @formatter:on
    }

    @Test
    fun findById() {
        val leadTimeConfig = authenticator.withDefaultUser {
            leadTimeConfigFactory.create()
        }!!

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .`when`()
                    .get("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", IdMatcher(leadTimeConfig.id))
                    .body("boardId", IdMatcher(leadTimeConfig.board.id!!))
                    .body("name", Matchers.equalTo(leadTimeConfig.name))
                    .body("startColumn", Matchers.equalTo(leadTimeConfig.startColumn))
                    .body("endColumn", Matchers.equalTo(leadTimeConfig.endColumn))
        // @formatter:on
    }

    @Test
    fun findByIdNotFound() {
        authenticator.withDefaultUser(boardFactory::create)

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .`when`()
                    .get("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(notFound())
        // @formatter:on
    }
}
