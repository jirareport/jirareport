package br.com.jiratorio.integration.holiday

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.resolver.SpecificationResolver
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.factory.entity.HolidayFactory
import br.com.jiratorio.matcher.IdMatcher
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.format.DateTimeFormatter

@Tag("integration")
@ExtendWith(SpringExtension::class, SpecificationResolver::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SearchHolidayIntegrationTest @Autowired constructor(
        private val holidayFactory: HolidayFactory,
        private val boardFactory: BoardFactory,
        private val authenticator: Authenticator
) {

    @Test
    fun findAllHolidays() {
        val (id) = authenticator.withDefaultUser {
            val boardExample = boardFactory.create()
            holidayFactory.create(10) { it.board = boardExample }

            boardExample
        }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .get("/boards/{id}/holidays", id)
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfElements", equalTo(10))
                .body("totalPages", equalTo(1))
                .body("content[0].id", notNullValue())
                .body("content[0].date", notNullValue())
                .body("content[0].description", notNullValue())
                .body("content[0].boardId", IdMatcher(id!!))
                .body("content.findAll { it.boardId == 1 }", hasSize<Any>(10))
         // @formatter:on
    }

    @Test
    fun findById() {
        val holiday = authenticator.withDefaultUser { holidayFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .get("/boards/1/holidays/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("id", IdMatcher(holiday.id!!))
                .body("date", equalTo(holiday.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .body("description", equalTo(holiday.description))
                .body("boardId", IdMatcher(holiday.board.id!!))
         // @formatter:on
    }

    @Test
    fun findByIdNotFound() {
        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .get("/boards/1/holidays/999")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on
    }

}
