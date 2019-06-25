package br.com.jiratorio.integration.dynamicfieldconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchDynamicFieldConfigIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `test find all`() {
        val dynamicFieldConfigs = authenticator.withDefaultUser {
            val board = boardFactory.create()
            dynamicFieldConfigFactory.create(10) {
                it.board = board
            }
        }

        val dynamicFieldConfig = dynamicFieldConfigs.first()

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/dynamic-field-configs")
            }
            then {
                statusCode(SC_OK)
                body("$", Matchers.hasSize<Int>(10))
                body("[0].name", Matchers.equalTo(dynamicFieldConfig.name))
                body("[0].field", Matchers.equalTo(dynamicFieldConfig.field))
            }
        }
    }

    @Test
    fun `test find all board not found`() {
        authenticator.withDefaultUser {
            val board = boardFactory.create()
            dynamicFieldConfigFactory.create(10) {
                it.board = board
            }

            board
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/99/dynamic-field-configs")
            }
            then {
                spec(notFound())
            }
        }
    }

}
