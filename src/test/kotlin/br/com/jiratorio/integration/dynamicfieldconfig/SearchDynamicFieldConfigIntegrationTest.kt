package br.com.jiratorio.integration.dynamicfieldconfig

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.DynamicFieldConfigFactory
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@IntegrationTest
class SearchDynamicFieldConfigIntegrationTest(
    private val boardFactory: BoardFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `test find all`() {
        val dynamicFieldConfigs = authenticator.withDefaultUser {
            val board = boardFactory.create()
            dynamicFieldConfigFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board
                )
            )
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
            dynamicFieldConfigFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    DynamicFieldConfigEntity::board to board
                )
            )

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
