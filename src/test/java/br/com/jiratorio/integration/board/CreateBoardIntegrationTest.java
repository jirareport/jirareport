package br.com.jiratorio.integration.board;

import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.repository.BoardRepository;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.request.CreateBoardRequest;
import br.com.jiratorio.factory.domain.request.CreateBoardRequestFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateBoardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CreateBoardRequestFactory createBoardRequestFactory;

    @Test
    public void createBoard() {
        CreateBoardRequest request = createBoardRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .header("location", Matchers.containsString("/boards/1"));
        // @formatter:on

        Board board = boardRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);

        Assertions.assertAll("boardContent",
                () -> Assertions.assertEquals(request.getName(), board.getName()),
                () -> Assertions.assertEquals(request.getExternalId(), board.getExternalId()),
                () -> Assertions.assertEquals("default_user", board.getOwner()));
    }

    @Test
    public void failInValidations() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(new CreateBoardRequest())
                .when()
                    .post("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("errors.find { it.field == 'externalId' }.defaultMessage", Matchers.is("must not be null"))
                    .body("errors.find { it.field == 'name' }.defaultMessage", Matchers.is("must not be blank"));
        // @formatter:on
    }
}
