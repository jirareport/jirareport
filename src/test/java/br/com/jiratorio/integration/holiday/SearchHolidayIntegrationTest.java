package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Holiday;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.factory.entity.HolidayFactory;
import br.com.jiratorio.matcher.IdMatcher;
import io.restassured.RestAssured;
import io.restassured.specification.ResponseSpecification;
import java.time.format.DateTimeFormatter;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchHolidayIntegrationTest {

    private final HolidayFactory holidayFactory;

    private final BoardFactory boardFactory;

    private final Authenticator authenticator;

    @Autowired
    SearchHolidayIntegrationTest(final HolidayFactory holidayFactory,
                                        final BoardFactory boardFactory,
                                        final Authenticator authenticator) {
        this.holidayFactory = holidayFactory;
        this.boardFactory = boardFactory;
        this.authenticator = authenticator;
    }

    @Test
    void findAllHolidays() {
        Board board = authenticator.withDefaultUser(() -> {
            Board boardExample = boardFactory.create();
            holidayFactory.create(10, example -> {
                example.setBoard(boardExample);
            });

            return boardExample;
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/{id}/holidays", board.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("numberOfElements", Matchers.is(10))
                    .body("totalPages", Matchers.is(1))
                    .body("content[0].id", Matchers.notNullValue())
                    .body("content[0].date", Matchers.notNullValue())
                    .body("content[0].description", Matchers.notNullValue())
                    .body("content[0].boardId", IdMatcher.is(board.getId()))
                    .body("content.findAll { it.boardId == 1 }", Matchers.hasSize(10));
        // @formatter:on
    }

    @Test
    void findById() {
        Holiday holiday = authenticator.withDefaultUser(holidayFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/1/holidays/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", IdMatcher.is(holiday.getId()))
                    .body("date", Matchers.is(holiday.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .body("description", Matchers.is(holiday.getDescription()))
                    .body("boardId", IdMatcher.is(holiday.getBoard().getId()));
        // @formatter:on
    }

    @Test
    void findByIdNotFound(@NotFound final ResponseSpecification spec) {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }

}
