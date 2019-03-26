package br.com.jiratorio.mapper;

import br.com.jiratorio.aspect.annotation.ExecutionTime;
import br.com.jiratorio.domain.DynamicFieldConfig;
import br.com.jiratorio.domain.FluxColumn;
import br.com.jiratorio.domain.changelog.JiraChangelog;
import br.com.jiratorio.domain.changelog.JiraChangelogHistory;
import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import br.com.jiratorio.domain.duedate.DueDateType;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.entity.embedded.Changelog;
import br.com.jiratorio.domain.entity.embedded.DueDateHistory;
import br.com.jiratorio.service.ChangelogService;
import br.com.jiratorio.service.DueDateService;
import br.com.jiratorio.service.HolidayService;
import br.com.jiratorio.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class IssueMapper {

    private final HolidayService holidayService;

    private final ObjectMapper objectMapper;

    private final DueDateService dueDateService;

    private final ChangelogService changelogService;

    public IssueMapper(final HolidayService holidayService,
                       final ObjectMapper objectMapper,
                       final DueDateService dueDateService,
                       final ChangelogService changelogService) {
        this.holidayService = holidayService;
        this.objectMapper = objectMapper;
        this.dueDateService = dueDateService;
        this.changelogService = changelogService;
    }

    @ExecutionTime
    @Transactional
    public List<Issue> parse(final String rawText, final Board board) {
        JsonParser jsonParser = new JsonParser();
        JsonElement response = jsonParser.parse(rawText);
        JsonArray issues = response.getAsJsonObject().getAsJsonArray("issues");

        List<LocalDate> holidays = holidayService.findDaysByBoard(board.getId());

        FluxColumn fluxColumn = new FluxColumn(board);
        Set<String> startColumns = fluxColumn.getStartColumns();
        Set<String> endColumns = fluxColumn.getEndColumns();

        return StreamSupport.stream(issues.spliterator(), true)
                .map(issueRaw -> {
                    JsonObject issue = issueRaw.getAsJsonObject();

                    try {
                        return parseIssue(issue, board, holidays, startColumns, endColumns);
                    } catch (Exception e) {
                        log.error("Method=parse, info=Erro parseando issue, issue={}, err={}", getAsStringSafe(issue.get("key")), e.getMessage());
                        throw e;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Issue parseIssue(final JsonObject issue, final Board board,
                             final List<LocalDate> holidays, final Set<String> startColumns,
                             final Set<String> endColumns) {
        JsonObject fields = issue.get("fields").getAsJsonObject();

        List<JiraChangelogItem> changelogItems = extractChangelogItems(issue);
        List<Changelog> changelog = changelogService.parseChangelog(changelogItems, holidays, board.getIgnoreWeekend());

        LocalDateTime created = DateUtil.parseFromJira(fields.get("created").getAsString());

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        for (Changelog cl : changelog) {
            if (startDate == null && startColumns.contains(cl.getTo())) {
                startDate = cl.getCreated();
            }

            if (endDate == null && endColumns.contains(cl.getTo())) {
                endDate = cl.getCreated();
            }
        }

        if ("BACKLOG".equals(board.getStartColumn())) {
            startDate = created;
        }

        if (startDate == null || endDate == null) {
            return null;
        }

        Long leadTime = DateUtil.daysDiff(startDate, endDate, holidays, board.getIgnoreWeekend());

        String author = null;
        JsonObject creator = fields.getAsJsonObject("creator");
        if (creator != null) {
            author = getAsStringSafe(creator.get("displayName"));
        }

        Long deviationOfEstimate = null;
        List<DueDateHistory> dueDateHistory = null;

        DueDateType dueDateType = board.getDueDateType();
        if (!StringUtils.isEmpty(board.getDueDateCF()) && dueDateType != null) {
            dueDateHistory = dueDateService.extractDueDateHistory(board.getDueDateCF(), changelogItems);
            if (!dueDateHistory.isEmpty()) {
                deviationOfEstimate = dueDateType.calcDeviationOfEstimate(dueDateHistory, endDate, board.getIgnoreWeekend(), holidays);
            }
        }

        Long timeInImpediment = board.getImpedimentType() == null ? 0L : board.getImpedimentType()
                .timeInImpediment(board.getImpedimentColumns(), changelogItems, changelog, endDate, holidays, board.getIgnoreWeekend());

        String priority = null;
        if (fields.has("priority") && !fields.get("priority").isJsonNull() && fields.get("priority").isJsonObject()) {
            JsonObject priorityObj = fields.getAsJsonObject("priority");
            priority = getAsStringSafe(priorityObj.get("name"));
        }

        Map<String, String> dynamicFields = parseDynamicFields(board, fields);

        Long waitTime = 0L;
        Long touchTime = 0L;
        double pctEfficiency = 0;

        if (!CollectionUtils.isEmpty(board.getTouchingColumns()) && !CollectionUtils.isEmpty(board.getWaitingColumns())) {
            waitTime = calcWaitTime(changelog, board.getWaitingColumns(), holidays, board.getIgnoreWeekend());
            touchTime = calcTouchTime(changelog, board.getTouchingColumns(), holidays, board.getIgnoreWeekend());
            pctEfficiency = calcPctEfficiency(waitTime, touchTime);
        }

        return Issue.builder()
                .creator(author)
                .key(getAsStringSafe(issue.get("key")))
                .issueType(getAsStringSafe(fields.getAsJsonObject("issuetype").get("name")))
                .created(created)
                .startDate(startDate)
                .endDate(endDate)
                .leadTime(leadTime)
                .system(parseElement(fields, board.getSystemCF()))
                .epic(parseElement(fields, board.getEpicCF()))
                .estimated(parseElement(fields, board.getEstimateCF()))
                .project(parseElement(fields, board.getProjectCF()))
                .summary(fields.get("summary").getAsString())
                .changelog(changelog)
                .board(board)
                .deviationOfEstimate(deviationOfEstimate)
                .dueDateHistory(dueDateHistory)
                .impedimentTime(timeInImpediment)
                .priority(priority)
                .dynamicFields(dynamicFields)
                .waitTime(waitTime)
                .touchTime(touchTime)
                .pctEfficiency(pctEfficiency)
                .build();
    }

    @SneakyThrows
    private List<JiraChangelogItem> extractChangelogItems(final JsonObject issue) {
        JiraChangelog changelog = objectMapper.readValue(issue.getAsJsonObject("changelog").toString(), JiraChangelog.class);
        changelog.getHistories().forEach(cl -> cl.getItems().forEach(i -> i.setCreated(cl.getCreated())));
        return changelog.getHistories().parallelStream()
                .map(JiraChangelogHistory::getItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private String parseElement(final JsonObject fields, final String cf) {
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
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return getAsStringSafe(jsonObject.has("value") ? jsonObject.get("value") : jsonObject.get("name"));
        }

        return jsonElement.getAsString();
    }

    private String getAsStringSafe(final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }

        if (jsonElement.isJsonObject()) {
            JsonElement value = jsonElement.getAsJsonObject().get("value");
            return getAsStringSafe(value);
        }

        if (jsonElement.isJsonArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                sb.append(getAsStringSafe(element)).append(", ");
            }

            return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 2);
        }

        return jsonElement.getAsString();
    }

    private Map<String, String> parseDynamicFields(final Board board, final JsonObject fields) {
        if (board.getDynamicFields() == null || board.getDynamicFields().isEmpty()) {
            return null;
        }

        Map<String, String> dynamicFields = new HashMap<>();
        for (DynamicFieldConfig dynamicField : board.getDynamicFields()) {
            dynamicFields.put(dynamicField.getName(), getAsStringSafe(fields.get(dynamicField.getField())));
        }

        return dynamicFields;
    }

    private double calcPctEfficiency(final Long waitTime, final Long touchTime) {
        if (touchTime == 0 && waitTime == 0) {
            return 0D;
        }
        return ((double) touchTime / (touchTime + waitTime)) * 100;
    }

    private Long calcTouchTime(final List<Changelog> changelogItems,
                               final List<String> touchingColumns,
                               final List<LocalDate> holidays,
                               final Boolean ignoreWeekend) {
        return calcDurationInColumns(changelogItems, touchingColumns, holidays, ignoreWeekend);
    }

    private Long calcWaitTime(final List<Changelog> changelogItems,
                              final List<String> waitingColumns,
                              final List<LocalDate> holidays,
                              final Boolean ignoreWeekend) {
        return calcDurationInColumns(changelogItems, waitingColumns, holidays, ignoreWeekend);
    }

    private Long calcDurationInColumns(final List<Changelog> changelogItems,
                                       final List<String> touchingColumns,
                                       final List<LocalDate> holidays,
                                       final Boolean ignoreWeekend) {
        long time = 0L;

        for (Changelog changelogItem : changelogItems) {
            if (touchingColumns.contains(changelogItem.getTo().toUpperCase(DateUtil.LOCALE_BR))) {
                time += DateUtil.minutesDiff(changelogItem.getCreated(), changelogItem.getEndDate(), holidays, ignoreWeekend);
            }
        }

        return time;
    }
}
