package br.com.jiratorio.base.executionlistener

import br.com.jiratorio.base.annotation.LoadStubs
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import org.springframework.util.StreamUtils
import java.lang.reflect.AnnotatedElement
import java.nio.charset.Charset

class WireMockTestExecutionListener : TestExecutionListener {

    private val charset = Charset.forName("UTF-8")

    override fun beforeTestMethod(testContext: TestContext) {
        if (!testContext.applicationContext.containsBean("wireMockServer")) {
            return
        }

        val wireMockServer = testContext.applicationContext.getBean(WireMockServer::class.java)
        val resolver = PathMatchingResourcePatternResolver()

        val stubs = findStubs(testContext)
        if (stubs != null) {
            if (stubs.isEmpty()) {
                loadStubs(wireMockServer, resolver, testContext.testMethod.name)
            } else {
                stubs.forEach { loadStubs(wireMockServer, resolver, it) }
            }
        }
    }

    private fun findStubs(testContext: TestContext): Array<String>? {
        val methodStubs =
            AnnotationUtils.findAnnotation(testContext.testMethod as AnnotatedElement, LoadStubs::class.java)
        val classStubs = AnnotationUtils.findAnnotation(testContext.testClass, LoadStubs::class.java)

        if (methodStubs == null && classStubs == null) {
            return null
        }

        return (methodStubs?.value ?: emptyArray()) + (classStubs?.value ?: emptyArray())
    }

    private fun loadStubs(wireMockServer: WireMockServer, resolver: PathMatchingResourcePatternResolver, stub: String) =
        resolver.getResources("classpath:/stubs/$stub/**/*.json").forEach {
            wireMockServer.addStubMapping(StubMapping.buildFrom(StreamUtils.copyToString(it.inputStream, charset)))
        }

    override fun afterTestMethod(testContext: TestContext) {
        if (!testContext.applicationContext.containsBean("wireMockServer")) {
            return
        }

        val wireMockServer = testContext.applicationContext.getBean(WireMockServer::class.java)
        wireMockServer.stubMappings.forEach(wireMockServer::removeStub)
    }

}
