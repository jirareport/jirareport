package br.com.jiratorio.integration.holiday

import br.com.jiratorio.assert.HolidayAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.HolidayRequestFactory
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.repository.HolidayRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateHolidayIntegrationTest @Autowired constructor(
    private val holidayRequestFactory: HolidayRequestFactory,
    private val boardFactory: BoardFactory,
    private val holidayRepository: HolidayRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `create holiday`() {
        val request = holidayRequestFactory.build()
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

        val holiday = holidayRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        HolidayAssert(holiday).assertThat {
            hasDescription(request.description)
            hasDate(request.date)
        }
    }

    @Test
    fun `fail in validations`() {
        val request = object {
            val date: LocalDate = LocalDate.now()
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
                body("errors.find { it.field == 'description' }.messages", Matchers.contains("must not be blank"))
            }
        }
    }
}
