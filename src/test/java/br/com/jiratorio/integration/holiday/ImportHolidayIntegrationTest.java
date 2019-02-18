package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.base.BaseIntegrationTest;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.UserConfig;
import br.com.jiratorio.executionlistener.LoadStubs;
import br.com.jiratorio.factory.BoardFactory;
import br.com.jiratorio.factory.HolidayFactory;
import br.com.jiratorio.factory.UserConfigFactory;
import br.com.jiratorio.repository.HolidayRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.stream.IntStream;
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
public class ImportHolidayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayFactory holidayFactory;

    @Autowired
    private UserConfigFactory userConfigFactory;

    @Test
    @LoadStubs("holidays")
    public void importWithSuccess() {
        withDefaultUser(() -> boardFactory.create());

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .post("/boards/1/holidays/import")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED);
        // @formatter:on

        long count = holidayRepository.count();
        Assertions.assertEquals(5, count);

        WireMock.verify(1,
                WireMock.getRequestedFor(WireMock.urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", WireMock.equalTo("true"))
                        .withQueryParam("ano", WireMock.equalTo(String.valueOf(LocalDate.now().getYear())))
                        .withQueryParam("estado", WireMock.equalTo("SP"))
                        .withQueryParam("cidade", WireMock.equalTo("ARARAQUARA"))
                        .withQueryParam("token", WireMock.equalTo("super-secret-token")));
    }

    @Test
    @LoadStubs("holidays")
    public void importWithSuccessUserConfig() {
        UserConfig userConfig = withDefaultUser(() -> {
            boardFactory.create();
            return userConfigFactory.create();
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .post("/boards/1/holidays/import")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED);
        // @formatter:on

        long count = holidayRepository.count();
        Assertions.assertEquals(5, count);

        WireMock.verify(1,
                WireMock.getRequestedFor(WireMock.urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", WireMock.equalTo("true"))
                        .withQueryParam("ano", WireMock.equalTo(String.valueOf(LocalDate.now().getYear())))
                        .withQueryParam("estado", WireMock.equalTo(userConfig.getState()))
                        .withQueryParam("cidade", WireMock.equalTo(userConfig.getCity()))
                        .withQueryParam("token", WireMock.equalTo(userConfig.getHolidayToken())));
    }

    @Test
    @LoadStubs("holidays")
    public void alreadyBeenImported() {
        withDefaultUser(() -> {
            Board board = boardFactory.create();
            IntStream.range(1, 6).forEach(i ->
                    holidayFactory.create(empty -> {
                        empty.setDate(LocalDate.of(2019, i, 1));
                        empty.setBoard(board);
                    })
            );
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(defaultUserHeader())
                .when()
                    .post("/boards/1/holidays/import")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("message", Matchers.is("Holidays already imported"));
        // @formatter:on

        long count = holidayRepository.count();
        Assertions.assertEquals(5, count);

        WireMock.verify(1,
                WireMock.getRequestedFor(WireMock.urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", WireMock.equalTo("true"))
                        .withQueryParam("ano", WireMock.equalTo(String.valueOf(LocalDate.now().getYear())))
                        .withQueryParam("estado", WireMock.equalTo("SP"))
                        .withQueryParam("cidade", WireMock.equalTo("ARARAQUARA"))
                        .withQueryParam("token", WireMock.equalTo("super-secret-token")));
    }

}
