package br.com.jiratorio.executionlistener;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.StreamUtils;

@Slf4j
public class WireMockTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        if (!testContext.getApplicationContext().containsBean("wireMockServer")) {
            return;
        }

        WireMockServer wireMockServer = testContext.getApplicationContext().getBean(WireMockServer.class);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Method method = testContext.getTestMethod();
        List<String> stubs = findStubs(testContext);
        if (stubs != null) {
            if (stubs.isEmpty()) {
                loadStubs(wireMockServer, resolver, method.getName());
            } else {
                for (String stub : stubs) {
                    loadStubs(wireMockServer, resolver, stub);
                }
            }
        }
    }

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        if (!testContext.getApplicationContext().containsBean("wireMockServer")) {
            return;
        }

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

    private List<String> findStubs(final TestContext testContext) {
        LoadStubs methodStubs = AnnotationUtils.findAnnotation(testContext.getTestMethod(), LoadStubs.class);
        LoadStubs classStubs = AnnotationUtils.findAnnotation(testContext.getTestClass(), LoadStubs.class);

        if (methodStubs == null && classStubs == null) {
            return null;
        }

        List<String> stubs = new ArrayList<>();
        stubs.addAll(methodStubs == null ? Collections.emptyList() : Arrays.asList(methodStubs.value()));
        stubs.addAll(classStubs == null ? Collections.emptyList() : Arrays.asList(classStubs.value()));
        return stubs;
    }

}
