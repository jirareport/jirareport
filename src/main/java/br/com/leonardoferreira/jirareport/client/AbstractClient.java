package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.client.util.RestTemplateUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author leferreira
 * @since 7/28/17 11:15 AM
 */
public class AbstractClient extends RestTemplateUtil {

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
}
