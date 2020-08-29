package br.com.jiratorio.integration.userconfig

import br.com.jiratorio.Authenticator
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.UserConfigFactory
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test

@IntegrationTest
internal class FindUserConfigIntegrationTest(
    private val authenticator: Authenticator,
    private val userConfigFactory: UserConfigFactory
) {

    @Test
    fun `find current user config`() {
        val userConfig = authenticator.withDefaultUser { userConfigFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/users/me/configs")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("state", equalTo(userConfig.state))
                body("city", equalTo(userConfig.city))
                body("holidayToken", equalTo(userConfig.holidayToken))
                body("leadTimeChartType", equalTo(userConfig.leadTimeChartType.name))
                body("throughputChartType", equalTo(userConfig.throughputChartType.name))
            }
        }
    }

    @Test
    fun `find not customized current user config`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/users/me/configs")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("state", nullValue())
                body("city", nullValue())
                body("holidayToken", nullValue())
                body("leadTimeChartType", equalTo(ChartType.BAR.name))
                body("throughputChartType", equalTo(ChartType.DOUGHNUT.name))
            }
        }
    }
}
