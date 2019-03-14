package br.com.jiratorio.integration.holiday

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.HolidayRequestFactory
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.factory.entity.HolidayFactory
import br.com.jiratorio.repository.HolidayRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.format.DateTimeFormatter

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateHolidayIntegrationTest @Autowired constructor(
        private val holidayFactory: HolidayFactory,
        private val holidayRequestFactory: HolidayRequestFactory,
        private val boardFactory: BoardFactory,
        private val holidayRepository: HolidayRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun updateHoliday() {
        authenticator.withDefaultUser { holidayFactory.create() }
        val request = holidayRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .put("/boards/1/holidays/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        // @formatter:on

        val holiday = holidayRepository.findById(1L)
                .orElseThrow(::ResourceNotFound)

        holiday.apply {
            assertThat(description)
                    .isEqualTo(request.description)
            assertThat(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .isEqualTo(request.date)
        }
    }

    @Test
    fun failInValidations() {
        authenticator.withDefaultUser { holidayFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(HolidayRequest())
            .`when`()
                .put("/boards/1/holidays/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("errors.find { it.field == 'date' }.defaultMessage", Matchers.`is`("must not be blank"))
                .body("errors.find { it.field == 'description' }.defaultMessage", Matchers.`is`("must not be blank"))
         // @formatter:on
    }

    @Test
    fun updateHolidayNotFound() {
        authenticator.withDefaultUser { boardFactory.create() }
        val request = holidayRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .put("/boards/1/holidays/999")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on
    }
}
