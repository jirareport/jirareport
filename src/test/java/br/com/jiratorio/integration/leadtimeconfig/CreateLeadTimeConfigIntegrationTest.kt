package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.resolver.SpecificationResolver
import br.com.jiratorio.base.specification.NotFound
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.LeadTimeConfigRequestFactory
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.ResponseSpecification
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsString
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
internal class CreateLeadTimeConfigIntegrationTest @Autowired constructor(
        private val boardFactory: BoardFactory,
        private val leadTimeConfigRequestFactory: LeadTimeConfigRequestFactory,
        private val leadTimeConfigRepository: LeadTimeConfigRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun createLeadTimeConfig() {
        val request = authenticator.withDefaultUser {
            boardFactory.create()
            leadTimeConfigRequestFactory.build()
        }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .post("/boards/1/lead-time-configs")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", containsString("/boards/1/lead-time-configs/1"))
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
        authenticator.withDefaultUser { boardFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(LeadTimeConfigRequest())
            .`when`()
                .post("/boards/1/lead-time-configs")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("errors.find { it.field == 'name' }.defaultMessage", equalTo("must not be blank"))
                .body("errors.find { it.field == 'startColumn' }.defaultMessage", equalTo("must not be blank"))
                .body("errors.find { it.field == 'endColumn' }.defaultMessage", equalTo("must not be blank"))
         // @formatter:on
    }

    @Test
    fun createWithBoardNotFound(@NotFound spec: ResponseSpecification) {
        val request = leadTimeConfigRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .post("/boards/999/lead-time-configs")
            .then()
                .log().all()
                .spec(spec)
         // @formatter:on
    }
}
