package br.com.jiratorio.integration.userconfig

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.domain.ChartType
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.factory.entity.UserConfigFactory
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class FindUserConfigIntegrationTest @Autowired constructor(
        private val authenticator: Authenticator,
        private val userConfigFactory: UserConfigFactory
) {

    @Test
    fun findCurrentUserConfig() {
        val userConfig = authenticator.withDefaultUser { userConfigFactory.create() }

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .get("/users/me/configs")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(userConfig.state))
                .body("city", equalTo(userConfig.city))
                .body("holidayToken", equalTo(userConfig.holidayToken))
                .body("leadTimeChartType", equalTo(userConfig.leadTimeChartType.name))
                .body("throughputChartType", equalTo(userConfig.throughputChartType.name))
         // @formatter:on
    }

    @Test
    fun findNotCustomizedCurrentUserConfig() {
        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
            .`when`()
                .get("/users/me/configs")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("state", nullValue())
                .body("city", nullValue())
                .body("holidayToken", nullValue())
                .body("leadTimeChartType", equalTo(ChartType.BAR.name))
                .body("throughputChartType", equalTo(ChartType.DOUGHNUT.name))
         // @formatter:on
    }
}
