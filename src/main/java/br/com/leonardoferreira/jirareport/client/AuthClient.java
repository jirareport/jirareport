package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUserVO;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author leferreira
 * @since 7/28/17 10:11 AM
 */
@Component
public class AuthClient extends AbstractClient {

    public String login(AccountForm accountForm) {
        RestTemplate restTemplate = getRestTemplate();

        String url = baseUrl + "/rest/auth/1/session";
        ResponseEntity<String> rawResponse = restTemplate.postForEntity(url, accountForm, String.class);

        JsonObject response = jsonParser.parse(rawResponse.getBody()).getAsJsonObject();
        JsonObject session = response.getAsJsonObject("session");

        String sessionName = session.get("name").getAsString();
        String sessionValue = session.get("value").getAsString();

        return sessionName + "=" + sessionValue;
    }

    public CurrentUserVO getCurrentUser(String token) {
        RestTemplate restTemplate = getRestTemplate(token);

        String url = baseUrl + "/rest/api/2/myself";
        ResponseEntity<String> rawResponse = restTemplate.getForEntity(url, String.class);

        JsonObject response = jsonParser.parse(rawResponse.getBody()).getAsJsonObject();

        return new CurrentUserVO(
                response.get("displayName").getAsString(),
                response.get("emailAddress").getAsString());
    }
}
