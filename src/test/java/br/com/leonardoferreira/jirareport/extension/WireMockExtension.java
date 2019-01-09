package br.com.leonardoferreira.jirareport.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StreamUtils;

@Slf4j
public class WireMockExtension implements BeforeEachCallback, BeforeAllCallback, AfterEachCallback, AfterAllCallback {

    private WireMockServer wireMockServer;

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        wireMockServer = new WireMockServer(WireMockConfiguration
                .options().port(8888));
        wireMockServer.start();
    }

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        wireMockServer.stop();
    }

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Method method = context.getRequiredTestMethod();
        LoadStubs loadStubs = method.getAnnotation(LoadStubs.class);
        if (loadStubs != null) {
            if (ArrayUtils.isEmpty(loadStubs.value())) {
                loadStubs(resolver, method.getName());
            } else {
                for (String stub : loadStubs.value()) {
                    loadStubs(resolver, stub);
                }
            }
        }
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        wireMockServer.getStubMappings()
                .forEach(wireMockServer::removeStub);
    }

    private void loadStubs(final PathMatchingResourcePatternResolver resolver, final String stub) throws IOException {
        String locationPattern = "classpath:/stubs/" + stub + "/**/*.json";
        try {
            Resource[] resources = resolver.getResources(locationPattern);
            for (Resource resource : resources) {
                wireMockServer.addStubMapping(StubMapping
                        .buildFrom(StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"))));
            }
        } catch (FileNotFoundException e) {
            log.error("e={}", e.getMessage());
        }
    }
}
