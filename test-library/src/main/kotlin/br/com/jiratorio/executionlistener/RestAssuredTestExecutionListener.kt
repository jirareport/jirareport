package br.com.jiratorio.executionlistener

import io.restassured.RestAssured
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class RestAssuredTestExecutionListener : TestExecutionListener {

    override fun beforeTestMethod(testContext: TestContext) {
        RestAssured.port = testContext.applicationContext.environment
            .getProperty("local.server.port")?.toInt() ?: 0
    }

}
