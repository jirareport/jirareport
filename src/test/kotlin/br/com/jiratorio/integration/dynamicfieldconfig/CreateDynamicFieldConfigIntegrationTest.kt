package br.com.jiratorio.integration.dynamicfieldconfig

import br.com.jiratorio.assert.DynamicFieldConfigAssert
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.request.DynamicFieldConfigRequestFactory
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateDynamicFieldConfigIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigRequestFactory: DynamicFieldConfigRequestFactory,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `test create dynamic field config`() {
        val board = authenticator.withDefaultUser {
            boardFactory.create()
        }

        val request = dynamicFieldConfigRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/1/dynamic-field-configs")
            }
            then {
                statusCode(SC_CREATED)
                header("location", Matchers.containsString("/boards/1/dynamic-field-configs/1"))
            }
        }

        val dynamicFieldConfig = dynamicFieldConfigRepository.findByIdOrNull(1L) ?: throw ResourceNotFound()

        DynamicFieldConfigAssert(dynamicFieldConfig).assertThat {
            hasName(request.name)
            hasField(request.field)
            hasBoard(board)
        }
    }

    @Test
    fun `test fail in validations`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(
                    mapOf(
                        "name" to "",
                        "field" to ""
                    )
                )
            }
            on {
                post("/boards/1/dynamic-field-configs")
            }
            then {
                statusCode(SC_BAD_REQUEST)
                body("name", Matchers.contains("must not be blank"))
                body("field", Matchers.contains("must not be blank"))
            }
        }
    }

    @Test
    fun `test board not found`() {
        val request = dynamicFieldConfigRequestFactory.create()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                post("/boards/1/dynamic-field-configs")
            }
            then {
                spec(notFound())
            }
        }

    }
}
