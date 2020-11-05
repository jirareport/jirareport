package br.com.jiratorio.integration.dynamicfieldconfig

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import org.apache.http.HttpStatus.SC_NO_CONTENT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@IntegrationTest
class DeleteDynamicFieldConfigIntegrationTest(
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `test delete dynamic field config`() {
        authenticator.withDefaultUser {
            dynamicFieldConfigFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/dynamic-field-configs/1")
            }
            then {
                statusCode(SC_NO_CONTENT)
            }
        }

        assertThat(dynamicFieldConfigRepository.count())
            .isZero
    }

    @Test
    fun `test delete dynamic field config not found`() {
        authenticator.withDefaultUser {
            dynamicFieldConfigFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/dynamic-field-configs/2")
            }
            then {
                spec(notFound())
            }
        }

        assertThat(dynamicFieldConfigRepository.count())
            .isOne
    }

    @Test
    fun `test delete dynamic field config board not found`() {
        authenticator.withDefaultUser {
            dynamicFieldConfigFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/2/dynamic-field-configs/1")
            }
            then {
                spec(notFound())
            }
        }

        assertThat(dynamicFieldConfigRepository.count())
            .isOne
    }

}
