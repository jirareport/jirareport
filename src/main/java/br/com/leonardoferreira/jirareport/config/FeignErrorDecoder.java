package br.com.leonardoferreira.jirareport.config;

import br.com.leonardoferreira.jirareport.domain.vo.JiraError;
import br.com.leonardoferreira.jirareport.exception.JiraException;
import com.google.gson.Gson;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author lferreira on 08/06/18
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(final String methodKey,
                            final Response response) {
        Gson gson = new Gson();
        JiraError jiraError = gson.fromJson(response.body().asReader(), JiraError.class);

        return new JiraException(jiraError);
    }

}
