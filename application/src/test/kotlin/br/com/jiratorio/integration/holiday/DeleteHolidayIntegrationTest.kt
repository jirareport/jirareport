package br.com.jiratorio.integration.holiday

import br.com.jiratorio.Authenticator
import br.com.jiratorio.restassured.specification.notFound
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.HolidayFactory
import br.com.jiratorio.repository.HolidayRepository
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@IntegrationTest
internal class DeleteHolidayIntegrationTest(
    private val holidayFactory: HolidayFactory,
    private val holidayRepository: HolidayRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `delete holiday`() {
        authenticator.withDefaultUser { holidayFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/holidays/1")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }

        assertThat(holidayRepository.count()).isZero()
    }

    @Test
    fun `delete holiday not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/holidays/999")
            }
            then {
                spec(notFound())
            }
        }
    }
}