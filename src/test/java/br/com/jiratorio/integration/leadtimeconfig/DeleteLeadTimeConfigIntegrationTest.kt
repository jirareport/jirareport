package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
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
internal class DeleteLeadTimeConfigIntegrationTest @Autowired constructor(
        private val leadTimeConfigFactory: LeadTimeConfigFactory,
        private val leadTimeConfigRepository: LeadTimeConfigRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun deleteLeadTimeConfig() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/1/lead-time-configs/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
         // @formatter:on

        assertThat(leadTimeConfigRepository.count())
                .isZero()
    }

    @Test
    fun deleteWithBoardNotFound() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/999/lead-time-configs/1")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on

        assertThat(leadTimeConfigRepository.count())
                .isOne()
    }

    @Test
    fun deleteWithLeadTimeConfigNotFound() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .delete("/boards/1/lead-time-configs/9999")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on

        assertThat(leadTimeConfigRepository.count())
                .isOne()
    }
}
