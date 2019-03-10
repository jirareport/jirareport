package br.com.jiratorio.integration.leadtimeconfig;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory;
import br.com.jiratorio.repository.LeadTimeConfigRepository;
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
class DeleteLeadTimeConfigIntegrationTest {

    private final LeadTimeConfigFactory leadTimeConfigFactory;

    private final LeadTimeConfigRepository leadTimeConfigRepository;

    private final Authenticator authenticator;

    @Autowired
    DeleteLeadTimeConfigIntegrationTest(final LeadTimeConfigFactory leadTimeConfigFactory,
                                               final LeadTimeConfigRepository leadTimeConfigRepository,
                                               final Authenticator authenticator) {
        this.leadTimeConfigFactory = leadTimeConfigFactory;
        this.leadTimeConfigRepository = leadTimeConfigRepository;
        this.authenticator = authenticator;
    }

    @Test
    void deleteLeadTimeConfig() {
        authenticator.doWithDefaultUser(leadTimeConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void deleteWithBoardNotFound(@NotFound final ResponseSpecification spec) {
        authenticator.doWithDefaultUser(leadTimeConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .delete("/boards/999/lead-time-configs/1")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on

        long count = leadTimeConfigRepository.count();
        Assertions.assertEquals(1, count);
    }

    @Test
    void deleteWithLeadTimeConfigNotFound(@NotFound final ResponseSpecification spec) {
        authenticator.doWithDefaultUser(leadTimeConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .delete("/boards/1/lead-time-configs/9999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on

        long count = leadTimeConfigRepository.count();
        Assertions.assertEquals(1, count);
    }
}
