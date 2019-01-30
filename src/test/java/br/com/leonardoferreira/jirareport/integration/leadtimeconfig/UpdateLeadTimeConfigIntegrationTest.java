package br.com.leonardoferreira.jirareport.integration.leadtimeconfig;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.request.LeadTimeConfigRequest;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.factory.LeadTimeConfigFactory;
import br.com.leonardoferreira.jirareport.factory.LeadTimeConfigRequestFactory;
import br.com.leonardoferreira.jirareport.repository.LeadTimeConfigRepository;
import br.com.leonardoferreira.jirareport.util.DateUtil;
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
public class UpdateLeadTimeConfigIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LeadTimeConfigRequestFactory leadTimeConfigRequestFactory;

    @Autowired
    private LeadTimeConfigFactory leadTimeConfigFactory;

    @Autowired
    private LeadTimeConfigRepository leadTimeConfigRepository;

    @Test
    public void updateLeadTimeConfig() {
        withDefaultUser(() -> leadTimeConfigFactory.create());
        LeadTimeConfigRequest request = leadTimeConfigRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        LeadTimeConfig leadTimeConfig = leadTimeConfigRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);

        Assertions.assertAll("leadTimeConfig content",
                () -> Assertions.assertEquals(request.getName(), leadTimeConfig.getName()),
                () -> Assertions.assertEquals(request.getStartColumn().toUpperCase(DateUtil.LOCALE_BR), leadTimeConfig.getStartColumn()),
                () -> Assertions.assertEquals(request.getEndColumn().toUpperCase(DateUtil.LOCALE_BR), leadTimeConfig.getEndColumn()));
    }

    @Test
    public void failInValidations() {
        withDefaultUser(() -> leadTimeConfigFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(new LeadTimeConfigRequest())
                .when()
                    .put("/boards/1/lead-time-configs/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("errors.find { it.field == 'name' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'startColumn' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'endColumn' }.defaultMessage", Matchers.is("must not be blank"));
        // @formatter:on
    }

    @Test
    public void updateWithBoardNotFound() {
        withDefaultUser(() -> leadTimeConfigFactory.create());
        LeadTimeConfigRequest request = leadTimeConfigRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/999/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}
