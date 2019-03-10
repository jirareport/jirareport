package br.com.jiratorio.integration.userconfig;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.domain.ChartType;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.factory.entity.UserConfigFactory;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FindUserConfigIntegrationTest {

    private final Authenticator authenticator;

    private final UserConfigFactory userConfigFactory;

    @Autowired
    FindUserConfigIntegrationTest(final Authenticator authenticator,
                                  final UserConfigFactory userConfigFactory) {
        this.authenticator = authenticator;
        this.userConfigFactory = userConfigFactory;
    }

    @Test
    void findCurrentUserConfig() {
        UserConfig userConfig = authenticator.withDefaultUser(userConfigFactory::create);

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/users/me/configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("state", Matchers.is(userConfig.getState()))
                    .body("city", Matchers.is(userConfig.getCity()))
                    .body("holidayToken", Matchers.is(userConfig.getHolidayToken()))
                    .body("leadTimeChartType", Matchers.is(userConfig.getLeadTimeChartType().name()))
                    .body("throughputChartType", Matchers.is(userConfig.getThroughputChartType().name()));
        // @formatter:on
    }

    @Test
    void findNotCustomizedCurrentUserConfig() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                .when()
                    .get("/users/me/configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("state", Matchers.nullValue())
                    .body("city", Matchers.nullValue())
                    .body("holidayToken", Matchers.nullValue())
                    .body("leadTimeChartType", Matchers.is(ChartType.BAR.name()))
                    .body("throughputChartType", Matchers.is(ChartType.DOUGHNUT.name()));
        // @formatter:on
    }
}
