package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.HolidayRequestFactory;
import br.com.jiratorio.repository.HolidayRepository;
import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.Holiday;
import br.com.jiratorio.domain.request.HolidayRequest;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.factory.entity.HolidayFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.format.DateTimeFormatter;
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
public class UpdateHolidayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private HolidayFactory holidayFactory;

    @Autowired
    private HolidayRequestFactory holidayRequestFactory;

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void updateHoliday() {
        withDefaultUser(() -> holidayFactory.create());
        HolidayRequest request = holidayRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void failInValidations() {
        withDefaultUser(() -> holidayFactory.create());
        HolidayRequest request = new HolidayRequest();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void updateHolidayNotFound() {
        withDefaultUser(() -> boardFactory.create());
        HolidayRequest request = holidayRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}
