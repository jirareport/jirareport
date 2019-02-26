package br.com.jiratorio.base.executionlistener;

import io.restassured.RestAssured;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class RestAssuredTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        String port = testContext.getApplicationContext()
                .getEnvironment().getProperty("local.server.port");
        if (port != null) {
            RestAssured.port = Integer.valueOf(port);
        }
    }

}
