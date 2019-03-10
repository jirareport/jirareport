package br.com.jiratorio.integration.leadtimeconfig;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.LeadTimeConfig;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory;
import br.com.jiratorio.matcher.IdMatcher;
import io.restassured.RestAssured;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchLeadTimeConfigIntegrationTest {

    private final LeadTimeConfigFactory leadTimeConfigFactory;

    private final BoardFactory boardFactory;

    private final Authenticator authenticator;

    @Autowired
    SearchLeadTimeConfigIntegrationTest(final LeadTimeConfigFactory leadTimeConfigFactory,
                                               final BoardFactory boardFactory,
                                               final Authenticator authenticator) {
        this.leadTimeConfigFactory = leadTimeConfigFactory;
        this.boardFactory = boardFactory;
        this.authenticator = authenticator;
    }

    @Test
    void findAllLeadTimeConfig() {
        authenticator.doWithDefaultUser(() -> {
            Board board = boardFactory.create();
            leadTimeConfigFactory.create(10, empty ->
                    empty.setBoard(board));
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/1/lead-time-configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("$", Matchers.hasSize(10))
                    .body("findAll { it.boardId == 1 }", Matchers.hasSize(10));
        // @formatter:on
    }

    @Test
    void findById() {
        LeadTimeConfig leadTimeConfig = authenticator.withDefaultUser(leadTimeConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", IdMatcher.is(leadTimeConfig.getId()))
                    .body("boardId", IdMatcher.is(leadTimeConfig.getBoard().getId()))
                    .body("name", Matchers.is(leadTimeConfig.getName()))
                    .body("startColumn", Matchers.is(leadTimeConfig.getStartColumn()))
                    .body("endColumn", Matchers.is(leadTimeConfig.getEndColumn()));

        // @formatter:on
    }

    @Test
    void findByIdNotFound(@NotFound final ResponseSpecification spec) {
        authenticator.doWithDefaultUser(boardFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
