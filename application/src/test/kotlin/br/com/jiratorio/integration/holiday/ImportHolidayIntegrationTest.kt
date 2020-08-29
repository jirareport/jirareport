package br.com.jiratorio.integration.holiday

import br.com.jiratorio.Authenticator
import br.com.jiratorio.annotation.LoadStubs
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.HolidayFactory
import br.com.jiratorio.factory.domain.entity.UserConfigFactory
import br.com.jiratorio.repository.HolidayRepository
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.time.LocalDate

@IntegrationTest
@LoadStubs(["holidays"])
internal class ImportHolidayIntegrationTest(
    private val boardFactory: BoardFactory,
    private val holidayRepository: HolidayRepository,
    private val holidayFactory: HolidayFactory,
    private val userConfigFactory: UserConfigFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `import with success`() {
        authenticator.withDefaultUser { boardFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("import", true)
            }
            on {
                post("/boards/1/holidays")
            }
            then {
                statusCode(HttpStatus.SC_CREATED)
            }
        }

        assertThat(holidayRepository.count())
            .isEqualTo(5)

        verify(
            1,
            getRequestedFor(urlPathEqualTo("/holiday-api"))
                .withQueryParam("json", equalTo("true"))
                .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                .withQueryParam("estado", equalTo("SP"))
                .withQueryParam("cidade", equalTo("ARARAQUARA"))
                .withQueryParam("token", equalTo("super-secret-token"))
        )
    }

    @Test
    fun `import with success user config`() {
        val userConfig = authenticator.withDefaultUser {
            boardFactory.create()
            userConfigFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("import", true)
            }
            on {
                post("/boards/1/holidays")
            }
            then {
                statusCode(HttpStatus.SC_CREATED)
            }
        }

        assertThat(holidayRepository.count()).isEqualTo(5)

        verify(
            1,
            getRequestedFor(urlPathEqualTo("/holiday-api"))
                .withQueryParam("json", equalTo("true"))
                .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                .withQueryParam("estado", equalTo(userConfig.state))
                .withQueryParam("cidade", equalTo(userConfig.city))
                .withQueryParam("token", equalTo(userConfig.holidayToken))
        )
    }

    @Test
    fun `already been imported`() {
        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()
            for (i in 1..5) {
                holidayFactory.create(
                    modifyingFields = mapOf(
                        Holiday::date to LocalDate.of(2019, i, 1),
                        Holiday::board to defaultBoard
                    )
                )
            }
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("import", true)
            }
            on {
                post("/boards/1/holidays")
            }
            then {
                statusCode(HttpStatus.SC_BAD_REQUEST)
                body("message", equalTo<String>("The holidays have already been imported into this board"))
            }
        }

        assertThat(holidayRepository.count()).isEqualTo(5)

        verify(
            1,
            getRequestedFor(urlPathEqualTo("/holiday-api"))
                .withQueryParam("json", equalTo("true"))
                .withQueryParam("ano", equalTo(LocalDate.now().year.toString()))
                .withQueryParam("estado", equalTo("SP"))
                .withQueryParam("cidade", equalTo("ARARAQUARA"))
                .withQueryParam("token", equalTo("super-secret-token"))
        )
    }

}
