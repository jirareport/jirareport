package br.com.leonardoferreira.jirareport.integration.leadtimeconfig;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.factory.LeadTimeConfigFactory;
import br.com.leonardoferreira.jirareport.repository.LeadTimeConfigRepository;
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
public class DeleteLeadTimeConfigIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LeadTimeConfigFactory leadTimeConfigFactory;

    @Autowired
    private LeadTimeConfigRepository leadTimeConfigRepository;

    @Test
    public void deleteLeadTimeConfig() {
        withDefaultUser(() -> leadTimeConfigFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        long count = leadTimeConfigRepository.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void deleteWithBoardNotFound() {
        withDefaultUser(() -> leadTimeConfigFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/999/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on

        long count = leadTimeConfigRepository.count();
        Assertions.assertEquals(1, count);
    }

    @Test
    public void deleteWithLeadTimeConfigNotFound() {
        withDefaultUser(() -> leadTimeConfigFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .delete("/boards/1/lead-time-configs/9999")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on

        long count = leadTimeConfigRepository.count();
        Assertions.assertEquals(1, count);
    }
}
