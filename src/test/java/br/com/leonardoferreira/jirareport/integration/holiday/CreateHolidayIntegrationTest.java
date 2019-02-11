package br.com.leonardoferreira.jirareport.integration.holiday;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.request.HolidayRequest;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.factory.BoardFactory;
import br.com.leonardoferreira.jirareport.factory.HolidayRequestFactory;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
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
public class CreateHolidayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private HolidayRequestFactory holidayRequestFactory;

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void createHoliday() {
        HolidayRequest request = holidayRequestFactory.build();
        withDefaultUser(() -> boardFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .header(defaultUserHeader())
                .when()
                    .post("/boards/1/holidays")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .header("location", Matchers.containsString("/boards/1/holidays/1"));
        // @formatter:on

        Holiday holiday = holidayRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);
        Assertions.assertAll("holiday content",
                () -> Assertions.assertEquals(request.getDescription(), holiday.getDescription()),
                () -> Assertions.assertEquals(request.getDate(), holiday.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }

    @Test
    public void failInValidations() {
        HolidayRequest request = new HolidayRequest();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/boards/1/holidays")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("errors.find { it.field == 'date' }.defaultMessage", Matchers.is("must not be blank"))
                    .body("errors.find { it.field == 'description' }.defaultMessage", Matchers.is("must not be blank"));
        // @formatter:on
    }
}
