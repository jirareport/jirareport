package br.com.jiratorio.integration.userconfig

import br.com.jiratorio.Authenticator
import br.com.jiratorio.assertion.UserConfigAssert
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.UpdateUserConfigFactory
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.repository.UserConfigRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

@IntegrationTest
internal class UpdateUserConfigIntegrationTest(
    private val updateUserConfigFactory: UpdateUserConfigFactory,
    private val userConfigRepository: UserConfigRepository,
    private val authenticator: Authenticator,
) {

    @Test
    fun `update user config`() {
        val request = updateUserConfigFactory.create(
            modifyingFields = mapOf(
                UpdateUserConfigRequest::city to "São Paulo"
            )
        )

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                contentType(ContentType.JSON)
                body(request)
            }
            on {
                put("/users/me/configs")
            }
            then {
                statusCode(HttpStatus.SC_NO_CONTENT)
            }
        }

        val userConfig = userConfigRepository.findByUsername(authenticator.defaultUserName())
            ?: throw ResourceNotFound()

        UserConfigAssert.assertThat(userConfig)
            .hasHolidayToken(request.holidayToken)
            .hasState(request.state)
            .hasCity("SAO_PAULO")
            .hasLeadTimeChartType(request.leadTimeChartType)
            .hasThroughputChartType(request.throughputChartType)
    }
}
