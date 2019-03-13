package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.factory.entity.HolidayFactory;
import br.com.jiratorio.repository.HolidayRepository;
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
class DeleteHolidayIntegrationTest {

    private final HolidayFactory holidayFactory;

    private final HolidayRepository holidayRepository;

    private final Authenticator authenticator;

    @Autowired
    DeleteHolidayIntegrationTest(final HolidayFactory holidayFactory,
                                        final HolidayRepository holidayRepository,
                                        final Authenticator authenticator) {
        this.holidayFactory = holidayFactory;
        this.holidayRepository = holidayRepository;
        this.authenticator = authenticator;
    }

    @Test
    void deleteHoliday() {
        authenticator.withDefaultUser(holidayFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .delete("/boards/1/holidays/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        long count = holidayRepository.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    void deleteHolidayNotFound(@NotFound final ResponseSpecification spec) {
        // @formatter:off
        RestAssured
                .given()
                    .header(authenticator.defaultUserHeader())
                    .log().all()
                .when()
                    .delete("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
