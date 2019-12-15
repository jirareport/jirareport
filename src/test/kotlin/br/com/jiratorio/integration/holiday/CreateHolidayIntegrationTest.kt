package br.com.jiratorio.integration.holiday

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.request.HolidayRequestFactory
import br.com.jiratorio.repository.HolidayRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateHolidayIntegrationTest(
    private val holidayRequestFactory: HolidayRequestFactory,
    private val boardFactory: BoardFactory,
    private val holidayRepository: HolidayRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `create holiday`() {
        val request = holidayRequestFactory.create()
        authenticator.withDefaultUser { boardFactory.create() }

        restAssured {
            given {
                contentType(ContentType.JSON)
                body(request)
                header(authenticator.defaultUserHeader())
            }
            on {
                post("/boards/1/holidays")
            }
            then {
                statusCode(HttpStatus.SC_CREATED)
                header("location", Matchers.containsString("/boards/1/holidays/1"))
            }
        }

        val holiday = holidayRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        holiday.assertThat {
            hasDescription(request.description)
            hasDate(request.date)
        }
    }

    @Test
    fun `fail in validations`() {
        val request = object {
            val date: String = "26/12/1995"
            val description: String = ""
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/1/holidays")
            }
            then {
                statusCode(HttpStatus.SC_BAD_REQUEST)
                body("description", Matchers.contains("must not be blank"))
            }
        }
    }
}
