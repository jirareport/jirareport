package br.com.leonardoferreira.jirareport.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author lferreira
 * @since 7/28/17 12:52 PM
 */
@Component
public class IssueMapper {

    @Autowired
    private HolidayService holidayService;

    private final JsonParser jsonParser = new JsonParser();

    @ExecutionTime
    public List<Issue> parse(final String rawText, final Board board) {
        JsonElement response = jsonParser.parse(rawText);
        JsonArray issues = response.getAsJsonObject()
                .getAsJsonArray("issues");

        final List<String> holidays = holidayService.findByBoard(board.getId())
                .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        Set<String> startColumns = board.getStartColumns();
        Set<String> endColumns = board.getEndColumns();

        return StreamSupport.stream(issues.spliterator(), true)
                .map(issueRaw -> {
                    JsonObject issue = issueRaw.getAsJsonObject();

                    JsonObject fields = issue.get("fields").getAsJsonObject();
                    List<Changelog> changelog = getChangelog(issue, holidays);

                    String created = getDateAsString(fields.get("created"));

                    String startDate = "BACKLOG".equals(board.getStartColumn()) ? created : null;
                    String endDate = null;

                    for (Changelog cl : changelog) {
                        if (startDate == null && startColumns.contains(cl.getTo())) {
                            startDate = DateUtil.toENDate(cl.getCreated());
                        }

                        if (endDate == null && endColumns.contains(cl.getTo())) {
                            endDate = DateUtil.toENDate(cl.getCreated());
                        }
                    }

                    if (startDate == null || endDate == null) {
                        return null;
                    }

                    String epicField = board.getEpicCF();
                    String estimateField = board.getEstimateCF();

                    String epic = StringUtils.isEmpty(epicField) ? null : getAsStringSafe(fields.get(epicField));
                    String estimated = null;
                    if (!StringUtils.isEmpty(estimateField) && !fields.get(estimateField).isJsonNull()) {
                        estimated = getAsStringSafe(fields.get(estimateField).getAsJsonObject().get("value"));
                    }

                    Long leadTime = DateUtil.daysDiff(startDate, endDate, holidays);

                    Issue issueVO = new Issue();
                    issueVO.setKey(issue.get("key").getAsString());

                    JsonObject creator = fields.getAsJsonObject("creator");
                    if (creator != null) {
                        issueVO.setCreator(creator.get("displayName").getAsString());
                    }

                    issueVO.setIssueType(getAsStringSafe(fields.getAsJsonObject("issuetype").get("name")));
                    issueVO.setCreated(DateUtil.displayFormat(created));
                    issueVO.setStartDate(DateUtil.displayFormat(startDate));
                    issueVO.setEndDate(DateUtil.displayFormat(endDate));
                    issueVO.setLeadTime(leadTime);
                    issueVO.setSystem(getElement(fields, board.getSystemCF()));
                    issueVO.setEpic(epic);
                    issueVO.setEstimated(estimated);
                    issueVO.setProject(getElement(fields, board.getProjectCF()));
                    issueVO.setSummary(fields.get("summary").getAsString());
                    issueVO.setChangelog(changelog);

                    return issueVO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Changelog> getChangelog(final JsonObject issue, final List<String> holidays) {
        JsonArray histories = issue.getAsJsonObject("changelog").getAsJsonArray("histories");

        List<Changelog> collect = StreamSupport.stream(histories.spliterator(), true)
                .map(historyRaw -> {
                    JsonObject history = historyRaw.getAsJsonObject();
                    JsonObject item = getItem(history);
                    if (item == null) {
                        return null;
                    }

                    Changelog changelog = new Changelog();
                    changelog.setCreated(getDateAsString(history.get("created")));
                    changelog.setFrom(getAsStringSafe(item.get("from")));
                    changelog.setTo(getAsStringSafe(item.get("to")));

                    return changelog;
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
            current.setLeadTime(DateUtil.daysDiff(current.getCreated(), next.getCreated(), holidays));
            current.setCreated(DateUtil.displayFormat(current.getCreated()));
        }

        return collect;
    }

    private JsonObject getItem(final JsonObject history) {
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

    private String getElement(final JsonObject fields, final String cf) {
        if (StringUtils.isEmpty(cf)) {
            return null;
        }
        final JsonElement jsonElement = fields.get(cf);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }

        if (jsonElement.isJsonArray()) {
            JsonArray components = jsonElement.getAsJsonArray();
            if (components == null) {
                return null;
            }

            return StreamSupport.stream(components.spliterator(), true)
                    .map(component -> component.isJsonObject()
                            ? component.getAsJsonObject().get("name").getAsString()
                            : component.getAsString())
                    .findFirst().orElse(null);
        }

        if (jsonElement.isJsonObject()) {
            return getAsStringSafe(jsonElement.getAsJsonObject().get("value"));
        }

        return jsonElement.getAsString();
    }

    private String getAsStringSafe(final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString();
    }

    private String getDateAsString(final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString().substring(0, 10);
    }

}
