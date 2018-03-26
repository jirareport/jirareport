package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Project;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leferreira
 * @since 7/28/17 11:40 AM
 */
@Component
public class ProjectClient extends AbstractClient {

    public List<Project> findAll(String token) {
        RestTemplate restTemplate = getRestTemplate(token);

        String url = baseUrl + "/rest/api/2/project";
        ResponseEntity<String> rawResponse = restTemplate.getForEntity(url, String.class);

        JsonArray response = jsonParser.parse(rawResponse.getBody()).getAsJsonArray();
        List<String> projectsRaw = new ArrayList<>();

        for (JsonElement jsonElement : response) {
            projectsRaw.add(jsonElement.toString());
        }

        return projectsRaw.parallelStream()
                .map(project -> jsonParser.parse(project))
                .map(JsonElement::getAsJsonObject)
                .map(project -> {
                    Project projectVO = new Project();
                    projectVO.setId(project.get("id").getAsInt());
                    projectVO.setName(project.get("name").getAsString());

                    return projectVO;
                }).collect(Collectors.toList());
    }
}
