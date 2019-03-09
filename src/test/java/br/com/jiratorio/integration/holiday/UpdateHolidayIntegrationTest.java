package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.entity.Holiday;
import br.com.jiratorio.domain.request.HolidayRequest;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.HolidayRequestFactory;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.factory.entity.HolidayFactory;
import br.com.jiratorio.repository.HolidayRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import java.time.format.DateTimeFormatter;
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
class UpdateHolidayIntegrationTest {

    private final HolidayFactory holidayFactory;

    private final HolidayRequestFactory holidayRequestFactory;

    private final BoardFactory boardFactory;

    private final HolidayRepository holidayRepository;

    private final Authenticator authenticator;

    @Autowired
    UpdateHolidayIntegrationTest(final HolidayFactory holidayFactory,
                                        final HolidayRequestFactory holidayRequestFactory,
                                        final BoardFactory boardFactory,
                                        final HolidayRepository holidayRepository,
                                        final Authenticator authenticator) {
        this.holidayFactory = holidayFactory;
        this.holidayRequestFactory = holidayRequestFactory;
        this.boardFactory = boardFactory;
        this.holidayRepository = holidayRepository;
        this.authenticator = authenticator;
    }

    @Test
    void updateHoliday() {
        authenticator.doWithDefaultUser(holidayFactory::create);
        HolidayRequest request = holidayRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1/holidays/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        Holiday holiday = holidayRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);

        Assertions.assertAll("holiday content",
                () -> Assertions.assertEquals(request.getDescription(), holiday.getDescription()),
                () -> Assertions.assertEquals(request.getDate(), holiday.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }

    @Test
    void failInValidations() {
        authenticator.doWithDefaultUser(holidayFactory::create);
        HolidayRequest request = new HolidayRequest();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1/holidays/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("errors.find { it.field == 'date' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'description' }.defaultMessage", Matchers.is("must not be blank"));
        // @formatter:on
    }

    @Test
    void updateHolidayNotFound(@NotFound final ResponseSpecification spec) {
        authenticator.doWithDefaultUser(boardFactory::create);
        HolidayRequest request = holidayRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
