package br.com.leonardoferreira.jirareport.client.impl;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Changelog;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author leferreira
 * @since 7/28/17 12:52 PM
 */
@Component
public class IssueClientImpl extends AbstractClient implements IssueClient {

    private final ProjectService projectService;

    public IssueClientImpl(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public List<Issue> findAll(String token, IssueForm issueForm) {
        RestTemplate restTemplate = getRestTemplate(token);

        final Project project = projectService.findById(issueForm.getProjectId());

        String jql = createJQL(project, issueForm);

        String url = baseUrl + "/rest/api/2/search?jql=" + jql + "&expand=changelog";

        ResponseEntity<String> rawResponse = restTemplate.getForEntity(url, String.class);

        JsonElement response = jsonParser.parse(rawResponse.getBody());
        JsonArray issues = response.getAsJsonObject()
                .getAsJsonArray("issues");

        return StreamSupport.stream(issues.spliterator(), true)
                .map(issueRaw -> {
                    JsonObject issue = issueRaw.getAsJsonObject();

                    JsonObject fields = issue.get("fields").getAsJsonObject();
                    List<Changelog> changelog = getChangelog(issue);

                    String startDate = null;
                    String endDate = null;

                    for (Changelog cl : changelog) {
                        if (cl.getTo().equalsIgnoreCase(project.getStartColumn())) {
                            startDate = DateUtil.toENDate(cl.getCreated());
                        }

                        if (cl.getTo().equalsIgnoreCase(project.getEndColumn())) {
                            endDate = DateUtil.toENDate(cl.getCreated());
                        }
                    }

                    if (startDate == null || endDate == null) {
                        return null;
                    }

                    String epic = epicField.equals("") ? null : getAsStringSafe(fields.get(epicField));
                    String estimated = estimateField.equals("") ? null : (getAsStringSafe(fields.get(estimateField).isJsonNull() ?
                            null : fields.get(estimateField).getAsJsonObject().get("value")));
                    Long leadTime = DateUtil.daysDiff(startDate, endDate);

                    Issue issueVO = new Issue();
                    issueVO.setKey(issue.get("key").getAsString());

                    JsonObject creator = issue.getAsJsonObject("creator");
                    if (creator != null) {
                        issueVO.setCreator(creator.get("displayName").getAsString());
                    }

                    issueVO.setCreated(fields.get("created").getAsString());
                    issueVO.setUpdated(fields.get("updated").getAsString());
                    issueVO.setStartDate(DateUtil.displayFormat(startDate));
                    issueVO.setEndDate(DateUtil.displayFormat(endDate));
                    issueVO.setLeadTime(leadTime);
                    issueVO.setComponents(getComponents(fields));
                    issueVO.setEpic(epic);
                    issueVO.setEstimated(estimated);
                    issueVO.setSummary(fields.get("summary").getAsString());
                    issueVO.setChangelog(changelog);

                    return issueVO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String createJQL(Project project, IssueForm issueForm) {
        StringBuilder jql = new StringBuilder();
        jql.append("project = ").append(project.getId()).append(" ");
        jql.append("AND STATUS CHANGED TO \"").append(project.getEndColumn()).append("\" DURING(\"");
        jql.append(DateUtil.toENDate(issueForm.getStartDate())).append("\", \"");
        jql.append(DateUtil.toENDate(issueForm.getEndDate())).append("\")");

        return jql.toString();
    }

    private List<Changelog> getChangelog(JsonObject issue) {
        JsonArray histories = issue.getAsJsonObject("changelog").getAsJsonArray("histories");

        List<Changelog> collect = StreamSupport.stream(histories.spliterator(), true)
                .map(historyRaw -> {
                    JsonObject history = historyRaw.getAsJsonObject();
                    JsonObject item = getItem(history);
                    if (item != null) {
                        Changelog changelog = new Changelog();
                        changelog.setCreated(getDateAsString(history.get("created")));
                        changelog.setFrom(getAsStringSafe(item.get("from")));
                        changelog.setTo(getAsStringSafe(item.get("to")));

                        return changelog;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (int i = 0; i < collect.size(); i++) {
            Changelog current = collect.get(i);
            if (i + 1 == collect.size()) {
                current.setCreated(DateUtil.displayFormat(current.getCreated()));
                break;
            }

            Changelog next = collect.get(i + 1);
            current.setCycleTime(DateUtil.daysDiff(current.getCreated(), next.getCreated()));
            current.setCreated(DateUtil.displayFormat(current.getCreated()));
        }

        return collect;
    }

    private JsonObject getItem(JsonObject history) {
        JsonArray items = history.getAsJsonArray("items");

        return StreamSupport.stream(items.spliterator(), true)
                .map(itemRaw -> {
                    JsonObject item = itemRaw.getAsJsonObject();
                    if (!"status".equalsIgnoreCase(item.get("field").getAsString())) {
                        return null;
                    }

                    String fromString = getAsStringSafe(item.get("fromString"));
                    String toString = getAsStringSafe(item.get("toString"));

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("from", fromString);
                    jsonObject.addProperty("to", toString);
                    return jsonObject;
                }).filter(Objects::nonNull).findFirst().orElse(null);
    }

    private List<String> getComponents(JsonObject fields) {
        JsonArray components = fields.getAsJsonArray("components");
        if (components == null) {
            return new ArrayList<>();
        }

        return StreamSupport.stream(components.spliterator(), true)
                .map(component -> component.getAsJsonObject().get("name").getAsString())
                .collect(Collectors.toList());
    }

}
