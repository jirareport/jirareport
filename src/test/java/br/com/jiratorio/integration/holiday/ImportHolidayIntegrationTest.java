package br.com.jiratorio.integration.holiday;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.annotation.LoadStubs;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.factory.entity.HolidayFactory;
import br.com.jiratorio.factory.entity.UserConfigFactory;
import br.com.jiratorio.repository.HolidayRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.stream.IntStream;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@LoadStubs("holidays")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImportHolidayIntegrationTest {

    private final BoardFactory boardFactory;

    private final HolidayRepository holidayRepository;

    private final HolidayFactory holidayFactory;

    private final UserConfigFactory userConfigFactory;

    private final Authenticator authenticator;

    @Autowired
    ImportHolidayIntegrationTest(final BoardFactory boardFactory,
                                        final HolidayRepository holidayRepository,
                                        final HolidayFactory holidayFactory,
                                        final UserConfigFactory userConfigFactory,
                                        final Authenticator authenticator) {
        this.boardFactory = boardFactory;
        this.holidayRepository = holidayRepository;
        this.holidayFactory = holidayFactory;
        this.userConfigFactory = userConfigFactory;
        this.authenticator = authenticator;
    }

    @Test
    void importWithSuccess() {
        authenticator.withDefaultUser(boardFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void importWithSuccessUserConfig() {
        UserConfig userConfig = authenticator.withDefaultUser(() -> {
            boardFactory.create();
            return userConfigFactory.create();
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
    void alreadyBeenImported() {
        authenticator.withDefaultUser(() -> {
            Board board = boardFactory.create();
            IntStream.range(1, 6).forEach(i ->
                    holidayFactory.create(empty -> {
                        empty.setDate(LocalDate.of(2019, i, 1));
                        empty.setBoard(board);
                    })
            );
            return null;
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
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
