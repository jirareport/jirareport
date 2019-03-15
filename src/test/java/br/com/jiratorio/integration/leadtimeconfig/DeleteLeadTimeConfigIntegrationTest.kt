package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
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
    fun `delete lead time config`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/lead-time-configs/1")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }
        assertThat(leadTimeConfigRepository.count()).isZero()
    }

    @Test
    fun `delete with board not found`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/999/lead-time-configs/1")
            }
            then {
                spec(notFound())
            }

            assertThat(leadTimeConfigRepository.count()).isOne()
        }
    }

    @Test
    fun `delete with lead time config not found`() {
        authenticator.withDefaultUser { leadTimeConfigFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                delete("/boards/1/lead-time-configs/9999")
            }
            then {
                spec(notFound())
            }
        }

        assertThat(leadTimeConfigRepository.count()).isOne()
    }
}
