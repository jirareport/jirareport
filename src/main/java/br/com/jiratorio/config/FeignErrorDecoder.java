package br.com.jiratorio.config;

import br.com.jiratorio.domain.vo.JiraError;
import br.com.jiratorio.exception.JiraException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

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
