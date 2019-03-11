package br.com.jiratorio.integration.board;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.repository.BoardRepository;
import io.restassured.RestAssured;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteBoardIntegrationTest {

    private final BoardFactory boardFactory;

    private final BoardRepository boardRepository;

    private final Authenticator authenticator;

    @Autowired
    DeleteBoardIntegrationTest(final BoardFactory boardFactory,
                                      final BoardRepository boardRepository,
                                      final Authenticator authenticator) {
        this.boardFactory = boardFactory;
        this.boardRepository = boardRepository;
        this.authenticator = authenticator;
    }

    @Test
    void deleteBord() {
        authenticator.doWithDefaultUser(boardFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void deleteNonexistentBoard() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .delete("/boards/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        // @formatter:on
    }

    @Test
    void deleteBoardNotFound(@NotFound final ResponseSpecification notFoundSpec) {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .delete("/boards/9999")
                .then()
                    .log().all()
                    .spec(notFoundSpec);
        // @formatter:on
    }
}