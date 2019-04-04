package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.assert.LeadTimeConfigAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.LeadTimeConfigRequestFactory
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateLeadTimeConfigIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val leadTimeConfigRequestFactory: LeadTimeConfigRequestFactory,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `create lead time config`() {
        val request = authenticator.withDefaultUser {
            boardFactory.create()
            leadTimeConfigRequestFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/1/lead-time-configs")
            }
            then {
                statusCode(HttpStatus.SC_CREATED)
                header("location", containsString("/boards/1/lead-time-configs/1"))
            }
        }

        val leadTimeConfig = leadTimeConfigRepository.findById(1L)
            .orElseThrow(::ResourceNotFound)

        LeadTimeConfigAssert(leadTimeConfig).assertThat {
            hasName(request.name)

            hasStartColumn(request.startColumn)
            hasEndColumn(request.endColumn)
        }
    }

    @Test
    fun `fail in validations`() {
        authenticator.withDefaultUser { boardFactory.create() }

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
                post("/boards/1/lead-time-configs")
            }
            then {
                statusCode(HttpStatus.SC_BAD_REQUEST)
                body("errors.find { it.field == 'name' }.messages", contains("must not be blank"))
                body("errors.find { it.field == 'name' }.messages", contains("must not be blank"))
                body("errors.find { it.field == 'name' }.messages", contains("must not be blank"))
            }
        }
    }

    @Test
    fun `create with board not found`() {
        val request = leadTimeConfigRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/999/lead-time-configs")
            }
            then {
                spec(notFound())
            }
        }
    }
}
