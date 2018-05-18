package br.com.leonardoferreira.jirareport.mapper;

import static br.com.leonardoferreira.jirareport.util.DateUtil.DEFAULT_FORMATTER;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
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

    public List<Issue> parse(final String rawText, final Project project) {
        JsonElement response = jsonParser.parse(rawText);
        JsonArray issues = response.getAsJsonObject()
                .getAsJsonArray("issues");

        final List<String> holidays = holidayService.findByProject(project.getId())
                .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        Set<String> startcolumns = project.getStartColumns();
        Set<String> endcolumns = project.getEndColumns();

        return StreamSupport.stream(issues.spliterator(), true)
                .map(issueRaw -> {
                    JsonObject issue = issueRaw.getAsJsonObject();

                    JsonObject fields = issue.get("fields").getAsJsonObject();
                    List<Changelog> changelog = getChangelog(issue, holidays);

                    String startDate = null;
                    String endDate = null;

                    for (Changelog cl : changelog) {
                        if (startDate == null && startcolumns.contains(cl.getTo())) {
                            startDate = DateUtil.toENDate(cl.getCreated());
                        }

                        if (endDate == null && endcolumns.contains(cl.getTo())) {
                            endDate = DateUtil.toENDate(cl.getCreated());
                        }
                    }

                    if (startDate == null || endDate == null) {
                        return null;
                    }

                    String epicField = project.getEpicCF();
                    String estimateField = project.getEstimateCF();

                    String epic = StringUtils.isEmpty(epicField) ? null : getAsStringSafe(fields.get(epicField));
                    String estimated = null;
                    if (!StringUtils.isEmpty(estimateField) && !fields.get(estimateField).isJsonNull()) {
                        estimated = getAsStringSafe(fields.get(estimateField).getAsJsonObject().get("value"));
                    }

                    Long leadTime = daysDiff(startDate, endDate, holidays);

                    Issue issueVO = new Issue();
                    issueVO.setKey(issue.get("key").getAsString());

                    JsonObject creator = fields.getAsJsonObject("creator");
                    if (creator != null) {
                        issueVO.setCreator(creator.get("displayName").getAsString());
                    }

                    issueVO.setIssueType(getAsStringSafe(fields.getAsJsonObject("issuetype").get("name")));
                    issueVO.setStartDate(DateUtil.displayFormat(startDate));
                    issueVO.setEndDate(DateUtil.displayFormat(endDate));
                    issueVO.setLeadTime(leadTime);
                    issueVO.setSystem(getElement(fields, project.getSystemCF()));
                    issueVO.setEpic(epic);
                    issueVO.setEstimated(estimated);
                    issueVO.setProject(getElement(fields, project.getProjectCF()));
                    issueVO.setSummary(fields.get("summary").getAsString());
                    issueVO.setChangelog(changelog);

                    return issueVO;
                })
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
            current.setCycleTime(daysDiff(current.getCreated(), next.getCreated(), holidays));
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

    @SneakyThrows
    public Long daysDiff(final String startDate, final String endDate, final List<String> holidays) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        Calendar start = Calendar.getInstance();
        start.setTime(new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).parse(startDate));
        Calendar end = Calendar.getInstance();
        end.setTime(new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).parse(endDate));
        Long workingDays = 0L;
        while (!start.after(end)) {
            int day = start.get(Calendar.DAY_OF_WEEK);
            if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY) && !isHoliday(start, holidays)) {
                workingDays++;
            }
            start.add(Calendar.DATE, 1);
        }
        return workingDays;
    }

    private boolean isHoliday(final Calendar day, final List<String> holidays) {
        String aux = new SimpleDateFormat(DEFAULT_FORMATTER, DateUtil.LOCALE_BR).format(day.getTime());
        return holidays.contains(aux);
    }

}
