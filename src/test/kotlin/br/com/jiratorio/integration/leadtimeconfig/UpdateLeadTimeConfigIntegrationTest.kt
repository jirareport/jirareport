package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.factory.domain.request.LeadTimeConfigRequestFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateLeadTimeConfigIntegrationTest @Autowired constructor(
    private val leadTimeConfigRequestFactory: LeadTimeConfigRequestFactory,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `update lead time config`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }
        val request = leadTimeConfigRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                put("/boards/1/lead-time-configs/1")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }

        val leadTimeConfig = leadTimeConfigRepository.findByIdOrNull(1L)
            ?: throw ResourceNotFound()

        leadTimeConfig.assertThat {
            hasName(request.name)

            hasStartColumn(request.startColumn)
            hasEndColumn(request.endColumn)
        }
    }

    @Test
    fun `fail in validations`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        val request = object {
            val name: String = ""
            val startColumn: String = ""
            val endColumn: String = ""
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                put("/boards/1/lead-time-configs/1")
            }
            then {
                statusCode(HttpStatus.SC_BAD_REQUEST)
                body("name", contains("must not be blank"))
                body("startColumn", contains("must not be blank"))
                body("endColumn", contains("must not be blank"))
            }
        }
    }

    @Test
    fun `update with board not found`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }
        val request = leadTimeConfigRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                put("/boards/999/lead-time-configs/1")
            }
            then {
                spec(notFound())
            }
        }
    }
}
