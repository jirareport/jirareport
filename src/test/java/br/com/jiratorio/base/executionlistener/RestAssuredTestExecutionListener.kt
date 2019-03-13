package br.com.jiratorio.base.executionlistener

import io.restassured.RestAssured
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class RestAssuredTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestMethod(testContext: TestContext) {
        RestAssured.port = testContext.applicationContext.environment
                .getProperty("local.server.port")?.toInt() ?: 0
    }

}
