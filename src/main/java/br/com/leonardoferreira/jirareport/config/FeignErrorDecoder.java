package br.com.leonardoferreira.jirareport.config;

import br.com.leonardoferreira.jirareport.domain.vo.JiraError;
import br.com.leonardoferreira.jirareport.exception.JiraException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(final String methodKey, final Response response) {
        JiraError jiraError;
        try {
            jiraError = objectMapper.readValue(response.body().asInputStream(), JiraError.class);
        } catch (JsonParseException e) {
            jiraError = new JiraError();
            jiraError.setStatusCode((long) response.status());

            if (response.status() == 401) {
                jiraError.setMessage("Sua sessão expirou. Tente novamente");
            }
        }

        return new JiraException(jiraError);
    }

}
