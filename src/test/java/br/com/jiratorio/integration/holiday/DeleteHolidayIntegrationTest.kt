package br.com.jiratorio.integration.holiday

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.factory.entity.HolidayFactory
import br.com.jiratorio.repository.HolidayRepository
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DeleteHolidayIntegrationTest @Autowired constructor(
        private val holidayFactory: HolidayFactory,
        private val holidayRepository: HolidayRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun deleteHoliday() {
        authenticator.withDefaultUser { holidayFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/1/holidays/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
         // @formatter:on

        assertThat(holidayRepository.count())
                .isZero()
    }

    @Test
    fun deleteHolidayNotFound() {
        // @formatter:off
        RestAssured
            .given()
                .header(authenticator.defaultUserHeader())
                .log().all()
            .`when`()
                .delete("/boards/1/holidays/999")
            .then()
                .log().all()
                .spec(notFound())
        // @formatter:on
    }
}
