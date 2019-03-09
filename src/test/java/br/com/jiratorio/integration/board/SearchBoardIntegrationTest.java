package br.com.jiratorio.integration.board;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.matcher.IdMatcher;
import io.restassured.RestAssured;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchBoardIntegrationTest {

    private final BoardFactory boardFactory;

    private final Authenticator authenticator;

    @Autowired
    SearchBoardIntegrationTest(final BoardFactory boardFactory,
                                      final Authenticator authenticator) {
        this.boardFactory = boardFactory;
        this.authenticator = authenticator;
    }

    @Test
    void findAllBoardsOfCurrentUser() {
        authenticator.doWithDefaultUser(() -> boardFactory.create(10));
        authenticator.doWithUser("other_user", () -> boardFactory.create(5));

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("totalPages", Matchers.is(1))
                    .body("totalElements", Matchers.is(10))
                    .body("content.findAll { it.owner == 'default_user'}", Matchers.hasSize(10));
        // @formatter:on
    }

    @Test
    void findAllOwners() {
        authenticator.doWithDefaultUser(() -> boardFactory.create(5));
        authenticator.doWithUser("first_user", () -> boardFactory.create(5));
        authenticator.doWithUser("second_user", () -> boardFactory.create(5));

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/owners")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("$", Matchers.hasSize(3))
                    .body("find { it == 'default_user' }", Matchers.not(Matchers.isEmptyOrNullString()))
                    .body("find { it == 'first_user' }", Matchers.not(Matchers.isEmptyOrNullString()))
                    .body("find { it == 'second_user' }", Matchers.not(Matchers.isEmptyOrNullString()));
        // @formatter:on
    }

    @Test
    void filterBoardByName() {
        authenticator.doWithDefaultUser(() -> {
            boardFactory.create(5, empty -> empty.setName("Uniq Start Name"));
            boardFactory.create(5);
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .param("name", "start")
                .when()
                    .get("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("totalPages", Matchers.is(1))
                    .body("totalElements", Matchers.is(5))
                    .body("content.findAll { it.name == 'Uniq Start Name' }", Matchers.hasSize(5));
        // @formatter:on
    }

    @Test
    void findBoardsOfAllOwners() {
        authenticator.doWithDefaultUser(() -> boardFactory.create(5));
        authenticator.doWithUser("user2", () -> boardFactory.create(5));
        authenticator.doWithUser("user3", () -> boardFactory.create(5));
        authenticator.doWithUser("user4", () -> boardFactory.create(5));

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .param("owner", "all")
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("totalElements", Matchers.is(20))
                    .body("totalPages", Matchers.is(1))
                    .body("content.findAll { it.owner == 'default_user' }", Matchers.hasSize(5))
                    .body("content.findAll { it.owner == 'user2' }", Matchers.hasSize(5))
                    .body("content.findAll { it.owner == 'user3' }", Matchers.hasSize(5))
                    .body("content.findAll { it.owner == 'user4' }", Matchers.hasSize(5));
        // @formatter:on
    }

    @Test
    void findBoardsByOwner() {
        authenticator.doWithDefaultUser(() -> boardFactory.create(5));
        authenticator.doWithUser("user2", () -> boardFactory.create(5));
        authenticator.doWithUser("user3", () -> boardFactory.create(5));
        authenticator.doWithUser("user4", () -> boardFactory.create(5));

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .param("owner", "user3")
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("totalElements", Matchers.is(5))
                    .body("totalPages", Matchers.is(1))
                    .body("content.findAll { it.owner == 'user3' }", Matchers.hasSize(5));
        // @formatter:on
    }

    @Test
    void findBoardById() {
        Board board = authenticator.withDefaultUser(() -> boardFactory.create("fullBoard"));

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/{id}", board.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("name", Matchers.is(board.getName()))
                    .body("externalId", IdMatcher.is(board.getExternalId()))
                    .body("startColumn", Matchers.is(board.getStartColumn()))
                    .body("endColumn", Matchers.is(board.getEndColumn()))
                    .body("fluxColumn", Matchers.contains(board.getFluxColumn().toArray()))
                    .body("ignoreIssueType", Matchers.contains(board.getIgnoreIssueType().toArray()))
                    .body("epicCF", Matchers.is(board.getEpicCF()))
                    .body("estimateCF", Matchers.is(board.getEstimateCF()))
                    .body("systemCF", Matchers.is(board.getSystemCF()))
                    .body("projectCF", Matchers.is(board.getProjectCF()))
                    .body("ignoreWeekend", Matchers.is(board.getIgnoreWeekend()))
                    .body("impedimentType", Matchers.is(board.getImpedimentType().name()))
                    .body("impedimentColumns", Matchers.contains(board.getImpedimentColumns().toArray()))
                    .body("dynamicFields", Matchers.hasSize(board.getDynamicFields().size()))
                    .body("dueDateCF", Matchers.is(board.getDueDateCF()))
                    .body("dueDateType", Matchers.is(board.getDueDateType().name()));
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
                    .get("/boards/9999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
