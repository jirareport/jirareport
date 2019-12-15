package br.com.jiratorio.integration.holiday

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.HolidayFactory
import br.com.jiratorio.repository.HolidayRepository
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
