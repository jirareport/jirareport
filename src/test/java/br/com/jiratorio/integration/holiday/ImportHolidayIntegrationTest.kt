package br.com.jiratorio.integration.holiday

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.factory.entity.HolidayFactory
import br.com.jiratorio.factory.entity.UserConfigFactory
import br.com.jiratorio.repository.HolidayRepository
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.util.stream.IntStream

@Tag("integration")
@LoadStubs(["holidays"])
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ImportHolidayIntegrationTest @Autowired constructor(
        private val boardFactory: BoardFactory,
        private val holidayRepository: HolidayRepository,
        private val holidayFactory: HolidayFactory,
        private val userConfigFactory: UserConfigFactory,
        private val authenticator: Authenticator
) {

    @Test
    fun importWithSuccess() {
        authenticator.withDefaultUser { boardFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .post("/boards/1/holidays/import")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
         // @formatter:on

        assertThat(holidayRepository.count())
                .isEqualTo(5)

        verify(1,
                getRequestedFor(urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", equalTo("true"))
                        .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                        .withQueryParam("estado", equalTo("SP"))
                        .withQueryParam("cidade", equalTo("ARARAQUARA"))
                        .withQueryParam("token", equalTo("super-secret-token")))
    }

    @Test
    fun importWithSuccessUserConfig() {
        val userConfig = authenticator.withDefaultUser {
            boardFactory.create()
            userConfigFactory.create()
        }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .post("/boards/1/holidays/import")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
         // @formatter:on

        assertThat(holidayRepository.count())
                .isEqualTo(5)

        verify(1,
                getRequestedFor(urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", equalTo("true"))
                        .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                        .withQueryParam("estado", equalTo(userConfig.state))
                        .withQueryParam("cidade", equalTo(userConfig.city))
                        .withQueryParam("token", equalTo(userConfig.holidayToken)))
    }

    @Test
    fun alreadyBeenImported() {
        authenticator.withDefaultUser {
            val board = boardFactory.create()
            IntStream.range(1, 6).forEach { i ->
                holidayFactory.create { empty ->
                    empty.date = LocalDate.of(2019, i, 1)
                    empty.board = board
                }
            }
        }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .post("/boards/1/holidays/import")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", Matchers.equalTo("Holidays already imported"))
         // @formatter:on

        assertThat(holidayRepository.count())
                .isEqualTo(5)

        verify(1,
                getRequestedFor(urlPathEqualTo("/holiday-api/"))
                        .withQueryParam("json", equalTo("true"))
                        .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                        .withQueryParam("estado", equalTo("SP"))
                        .withQueryParam("cidade", equalTo("ARARAQUARA"))
                        .withQueryParam("token", equalTo("super-secret-token")))
    }

}
