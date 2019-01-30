package br.com.leonardoferreira.jirareport.integration.holiday;

import br.com.leonardoferreira.jirareport.base.BaseIntegrationTest;
import br.com.leonardoferreira.jirareport.factory.HolidayFactory;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
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
public class DeleteHolidayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private HolidayFactory holidayFactory;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void deleteHoliday() {
        withDefaultUser(() -> holidayFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
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
    public void deleteHolidayNotFound() {
        // @formatter:off
        RestAssured
                .given()
                    .header(defaultUserHeader())
                    .log().all()
                .when()
                    .delete("/boards/1/holidays/999")
                .then()
                    .log().all()
                    .spec(notFoundSpec());
        // @formatter:on
    }
}
