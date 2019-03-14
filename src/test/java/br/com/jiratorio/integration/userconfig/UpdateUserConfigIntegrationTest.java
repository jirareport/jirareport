package br.com.jiratorio.integration.userconfig;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.UpdateUserConfigFactory;
import br.com.jiratorio.factory.entity.AccountFactory;
import br.com.jiratorio.repository.UserConfigRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateUserConfigIntegrationTest {

    private final UpdateUserConfigFactory updateUserConfigFactory;

    private final UserConfigRepository userConfigRepository;

    private final Authenticator authenticator;

    @Autowired
    UpdateUserConfigIntegrationTest(final UpdateUserConfigFactory updateUserConfigFactory,
                                    final UserConfigRepository userConfigRepository,
                                    final Authenticator authenticator) {
        this.updateUserConfigFactory = updateUserConfigFactory;
        this.userConfigRepository = userConfigRepository;
        this.authenticator = authenticator;
    }

    @Test
    void updateUserConfig() {
        UpdateUserConfigRequest request = updateUserConfigFactory.build(empty -> {
            empty.setCity("São Paulo");
        });

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/users/me/configs")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        UserConfig userConfig = userConfigRepository.findByUsername(authenticator.defaultUserName())
                .orElseThrow(ResourceNotFound::new);

        Assertions.assertThat(userConfig.getHolidayToken())
                .isEqualTo(request.getHolidayToken());
        Assertions.assertThat(userConfig.getState())
                .isEqualTo(request.getState());
        Assertions.assertThat(userConfig.getCity())
                .isEqualTo("SAO_PAULO");
        Assertions.assertThat(userConfig.getLeadTimeChartType())
                .isEqualTo(request.getLeadTimeChartType());
        Assertions.assertThat(userConfig.getThroughputChartType())
                .isEqualTo(request.getThroughputChartType());
    }
}
