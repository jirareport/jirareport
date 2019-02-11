package br.com.jiratorio.integration.board;

import br.com.jiratorio.repository.BoardRepository;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.factory.BoardFactory;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void deleteBord() {
        withDefaultUser(() -> boardFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        long count = boardRepository.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void deleteNonexistentBoard() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        // @formatter:on
    }

    @Test
    public void deleteBoardNotFound() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/9999")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}
