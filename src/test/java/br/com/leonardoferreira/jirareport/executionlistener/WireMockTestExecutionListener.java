package br.com.leonardoferreira.jirareport.executionlistener;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.StreamUtils;

@Slf4j
public class WireMockTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        WireMockServer wireMockServer = testContext.getApplicationContext().getBean(WireMockServer.class);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Method method = testContext.getTestMethod();
        LoadStubs loadStubs = method.getAnnotation(LoadStubs.class);
        if (loadStubs != null) {
            if (ArrayUtils.isEmpty(loadStubs.value())) {
                loadStubs(wireMockServer, resolver, method.getName());
            } else {
                for (String stub : loadStubs.value()) {
                    loadStubs(wireMockServer, resolver, stub);
                }
            }
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        WireMockServer wireMockServer = testContext.getApplicationContext().getBean(WireMockServer.class);
        wireMockServer.getStubMappings()
                .forEach(wireMockServer::removeStub);
    }

    private void loadStubs(final WireMockServer wireMockServer, final PathMatchingResourcePatternResolver resolver, final String stub) {
        String locationPattern = "classpath:/stubs/" + stub + "/**/*.json";
        try {
            Resource[] resources = resolver.getResources(locationPattern);
            for (Resource resource : resources) {
                wireMockServer.addStubMapping(StubMapping
                        .buildFrom(StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"))));
            }
        } catch (IOException e) {
            log.error("e={}", e.getMessage());
        }
    }

}
