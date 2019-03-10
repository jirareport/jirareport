package br.com.jiratorio.integration.board;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.request.CreateBoardRequest;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.CreateBoardRequestFactory;
import br.com.jiratorio.repository.BoardRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateBoardIntegrationTest {

    private final BoardRepository boardRepository;

    private final CreateBoardRequestFactory createBoardRequestFactory;

    private final Authenticator authenticator;

    @Autowired
    CreateBoardIntegrationTest(final BoardRepository boardRepository,
                                      final CreateBoardRequestFactory createBoardRequestFactory,
                                      final Authenticator authenticator) {
        this.boardRepository = boardRepository;
        this.createBoardRequestFactory = createBoardRequestFactory;
        this.authenticator = authenticator;
    }

    @Test
    void createBoard() {
        CreateBoardRequest request = createBoardRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void failInValidations() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
