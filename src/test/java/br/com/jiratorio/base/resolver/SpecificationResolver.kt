package br.com.jiratorio.base.resolver

import br.com.jiratorio.base.specification.NotFound
import io.restassured.builder.ResponseSpecBuilder
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class SpecificationResolver : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
            parameterContext.isAnnotated(NotFound::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext?): Any? {
        if (parameterContext.isAnnotated(NotFound::class.java)) {
            return notFoundSpec()
        }

        return null
    }

    private fun notFoundSpec() = ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_NOT_FOUND)
            .expectBody("error", Matchers.`is`("Not Found"))
            .expectBody("message", Matchers.`is`("Resource Not Found"))
            .expectBody("status", Matchers.`is`(404))
            .build()

}
