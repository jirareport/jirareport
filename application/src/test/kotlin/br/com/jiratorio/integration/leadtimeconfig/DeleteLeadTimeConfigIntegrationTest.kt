package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.Authenticator
import br.com.jiratorio.restassured.specification.notFound
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@IntegrationTest
internal class DeleteLeadTimeConfigIntegrationTest(
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
        }

        assertThat(leadTimeConfigRepository.count()).isOne()
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
