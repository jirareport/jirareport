package br.com.jiratorio.integration.userconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.UpdateUserConfigFactory
import br.com.jiratorio.repository.UserConfigRepository
import io.restassured.http.ContentType
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
internal class UpdateUserConfigIntegrationTest @Autowired constructor(
        private val updateUserConfigFactory: UpdateUserConfigFactory,
        private val userConfigRepository: UserConfigRepository,
        private val authenticator: Authenticator
) {

    @Test
    fun `update user config`() {
        val request = updateUserConfigFactory.build { it.city = "São Paulo" }

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

        userConfig.apply {
            assertThat(holidayToken)
                    .isEqualTo(request.holidayToken)
            assertThat(state)
                    .isEqualTo(request.state)
            assertThat(city)
                    .isEqualTo("SAO_PAULO")
            assertThat(userConfig.leadTimeChartType)
                    .isEqualTo(request.leadTimeChartType)
            assertThat(userConfig.throughputChartType)
                    .isEqualTo(request.throughputChartType)
        }
    }
}
