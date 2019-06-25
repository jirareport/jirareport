package br.com.jiratorio.integration.dynamicfieldconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import org.apache.http.HttpStatus.SC_NO_CONTENT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteDynamicFieldConfigIntegrationTest @Autowired constructor(
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
            .isZero()
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
            .isOne()
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
            .isOne()
    }

}
