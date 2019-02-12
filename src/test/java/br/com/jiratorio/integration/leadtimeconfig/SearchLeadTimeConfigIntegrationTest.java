package br.com.jiratorio.integration.leadtimeconfig;

import br.com.jiratorio.matcher.IdMatcher;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.LeadTimeConfig;
import br.com.jiratorio.factory.BoardFactory;
import br.com.jiratorio.factory.LeadTimeConfigFactory;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchLeadTimeConfigIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LeadTimeConfigFactory leadTimeConfigFactory;

    @Autowired
    private BoardFactory boardFactory;

    @Test
    public void findAllLeadTimeConfig() {
        withDefaultUser(() -> {
            Board board = boardFactory.create();
            leadTimeConfigFactory.create(10, empty ->
                    empty.setBoard(board));
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void findById() {
        LeadTimeConfig leadTimeConfig = withDefaultUser(() -> leadTimeConfigFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void findByIdNotFound() {
        withDefaultUser(() -> boardFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .get("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}