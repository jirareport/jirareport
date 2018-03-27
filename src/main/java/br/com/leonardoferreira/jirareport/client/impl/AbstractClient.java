package br.com.leonardoferreira.jirareport.client.impl;

import br.com.leonardoferreira.jirareport.client.interceptor.HeaderRequestInterceptor;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leferreira
 * @since 7/28/17 11:15 AM
 */
public class AbstractClient {

    @Value("${jira.url}")
    protected String baseUrl;

    @Value("${jira.custom_fields.epic:}")
    protected String epicField;

    @Value("${jira.custom_fields.estimate:}")
    protected String estimateField;

    protected JsonParser jsonParser = new JsonParser();

    protected String getDateAsString(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString().substring(0, 10);
    }

    protected String getAsStringSafe(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString();
    }

    @SneakyThrows
    protected RestTemplate getRestTemplate() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    protected RestTemplate getRestTemplate(String token) {
        RestTemplate restTemplate = getRestTemplate();
        setHeader(restTemplate, token);
        return restTemplate;
    }

    protected void setHeader(RestTemplate restTemplate, String token) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("cookie", token));

        restTemplate.setInterceptors(interceptors);
    }
}
