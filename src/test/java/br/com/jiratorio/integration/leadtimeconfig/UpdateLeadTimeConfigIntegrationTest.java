package br.com.jiratorio.integration.leadtimeconfig;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.entity.LeadTimeConfig;
import br.com.jiratorio.domain.request.LeadTimeConfigRequest;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.LeadTimeConfigRequestFactory;
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory;
import br.com.jiratorio.repository.LeadTimeConfigRepository;
import br.com.jiratorio.util.DateUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateLeadTimeConfigIntegrationTest {

    private final LeadTimeConfigRequestFactory leadTimeConfigRequestFactory;

    private final LeadTimeConfigFactory leadTimeConfigFactory;

    private final LeadTimeConfigRepository leadTimeConfigRepository;

    private final Authenticator authenticator;

    @Autowired
    UpdateLeadTimeConfigIntegrationTest(final LeadTimeConfigRequestFactory leadTimeConfigRequestFactory,
                                               final LeadTimeConfigFactory leadTimeConfigFactory,
                                               final LeadTimeConfigRepository leadTimeConfigRepository,
                                               final Authenticator authenticator) {
        this.leadTimeConfigRequestFactory = leadTimeConfigRequestFactory;
        this.leadTimeConfigFactory = leadTimeConfigFactory;
        this.leadTimeConfigRepository = leadTimeConfigRepository;
        this.authenticator = authenticator;
    }

    @Test
    void updateLeadTimeConfig() {
        authenticator.doWithDefaultUser(leadTimeConfigFactory::create);
        LeadTimeConfigRequest request = leadTimeConfigRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void failInValidations() {
        authenticator.doWithDefaultUser(leadTimeConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void updateWithBoardNotFound(@NotFound final ResponseSpecification spec) {
        authenticator.withDefaultUser(leadTimeConfigFactory::create);
        LeadTimeConfigRequest request = leadTimeConfigRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/999/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
