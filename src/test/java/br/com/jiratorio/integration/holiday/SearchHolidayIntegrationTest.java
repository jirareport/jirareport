package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.matcher.IdMatcher;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Holiday;
import br.com.jiratorio.factory.BoardFactory;
import br.com.jiratorio.factory.HolidayFactory;
import io.restassured.RestAssured;
import java.time.format.DateTimeFormatter;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchHolidayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private HolidayFactory holidayFactory;

    @Autowired
    private BoardFactory boardFactory;

    @Test
    public void findAllHolidays() {
        Board board = withDefaultUser(() -> {
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
                    .header(defaultUserHeader())
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
    public void findById() {
        Holiday holiday = withDefaultUser(() -> holidayFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void findByIdNotFound() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .get("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }

}
