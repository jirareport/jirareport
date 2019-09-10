package br.com.jiratorio.integration.userconfig

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.UpdateUserConfigFactory
import br.com.jiratorio.repository.UserConfigRepository
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateUserConfigIntegrationTest @Autowired constructor(
    private val updateUserConfigFactory: UpdateUserConfigFactory,
    private val userConfigRepository: UserConfigRepository,
    private val authenticator: Authenticator
) {

    @Test
    fun `update user config`() {
        val request = updateUserConfigFactory.create {
            it.city = "São Paulo"
        }

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
            .orElseThrow(::ResourceNotFound)

        userConfig.assertThat {
            hasHolidayToken(request.holidayToken!!)
            hasState(request.state!!)
            hasCity("SAO_PAULO")

            hasLeadTimeChartType(request.leadTimeChartType!!)
            hasThroughputChartType(request.throughputChartType!!)
        }
    }
}
