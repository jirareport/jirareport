package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.resolver.SpecificationResolver
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.LeadTimeConfigRequestFactory
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class, SpecificationResolver::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateLeadTimeConfigIntegrationTest @Autowired constructor(
        private val leadTimeConfigRequestFactory: LeadTimeConfigRequestFactory,
        private val leadTimeConfigFactory: LeadTimeConfigFactory,
        private val leadTimeConfigRepository: LeadTimeConfigRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun updateLeadTimeConfig() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }
        val request = leadTimeConfigRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .put("/boards/1/lead-time-configs/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
         // @formatter:on

        val leadTimeConfig = leadTimeConfigRepository.findById(1L)
                .orElseThrow(::ResourceNotFound)

        leadTimeConfig.apply {
            assertThat(name)
                    .isEqualTo(request.name)
            assertThat(startColumn)
                    .isUpperCase()
                    .isEqualToIgnoringCase(request.startColumn)
            assertThat(endColumn)
                    .isUpperCase()
                    .isEqualToIgnoringCase(request.endColumn)
        }
    }

    @Test
    fun failInValidations() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(LeadTimeConfigRequest())
            .`when`()
                .put("/boards/1/lead-time-configs/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("errors.find { it.field == 'name' }.defaultMessage", equalTo("must not be blank"))
                .body("errors.find { it.field == 'startColumn' }.defaultMessage", equalTo("must not be blank"))
                .body("errors.find { it.field == 'endColumn' }.defaultMessage", equalTo("must not be blank"))
         // @formatter:on
    }

    @Test
    fun updateWithBoardNotFound() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }
        val request = leadTimeConfigRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .put("/boards/999/lead-time-configs/1")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on
    }
}
