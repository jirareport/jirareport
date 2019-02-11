package br.com.jiratorio.integration.leadtimeconfig;

import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.repository.LeadTimeConfigRepository;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.LeadTimeConfig;
import br.com.jiratorio.domain.request.LeadTimeConfigRequest;
import br.com.jiratorio.factory.BoardFactory;
import br.com.jiratorio.factory.LeadTimeConfigRequestFactory;
import br.com.jiratorio.util.DateUtil;
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
public class CreateLeadTimeConfigIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private LeadTimeConfigRequestFactory leadTimeConfigRequestFactory;

    @Autowired
    private LeadTimeConfigRepository leadTimeConfigRepository;

    @Test
    public void createLeadTimeConfig() {
        LeadTimeConfigRequest request = withDefaultUser(() -> {
            boardFactory.create();
            return leadTimeConfigRequestFactory.build();
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/boards/1/lead-time-configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .header("location", Matchers.containsString("/boards/1/lead-time-configs/1"));
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
        withDefaultUser(() -> boardFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(new LeadTimeConfigRequest())
                .when()
                    .post("/boards/1/lead-time-configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("errors.find { it.field == 'name' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'startColumn' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'endColumn' }.defaultMessage", Matchers.is("must not be blank"));
        // @formatter:on
    }

    @Test
    public void createWithBoardNotFound() {
        LeadTimeConfigRequest request = leadTimeConfigRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/boards/999/lead-time-configs")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}
