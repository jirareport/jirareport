package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.DueDateHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelog;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lferreira
 * @since 7/28/17 12:52 PM
 */
@Slf4j
@Component
public class IssueMapper {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ObjectMapper objectMapper;

    private final JsonParser jsonParser = new JsonParser();

    @ExecutionTime
    @Transactional
    public List<Issue> parse(final String rawText, final Board board) {
        JsonElement response = jsonParser.parse(rawText);
        JsonArray issues = response.getAsJsonObject().getAsJsonArray("issues");

        final List<String> holidays = holidayService.findByBoard(board.getId())
                .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        Set<String> startColumns = CalcUtil.calcStartColumns(board);
        Set<String> endColumns = CalcUtil.calcEndColumns(board);

        return StreamSupport.stream(issues.spliterator(), true)
                .map(issueRaw -> {
                    JsonObject issue = issueRaw.getAsJsonObject();

                    JsonObject fields = issue.get("fields").getAsJsonObject();

                    List<JiraChangelogItem> changelogItems = extractChangelogItems(issue);
                    List<Changelog> changelog = parseChangelog(changelogItems, holidays, board.getIgnoreWeekend());

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

                    Long differenceFirstAndLastDueDate = null;
                    List<DueDateHistory> dueDateHistory = null;

                    if (Boolean.TRUE.equals(board.getCalcDueDate())) {
                        dueDateHistory = parseDueDateHistory(changelogItems);
                        if (!dueDateHistory.isEmpty()) {
                            LocalDate firstDueDate = dueDateHistory.get(0).getDueDate();
                            LocalDate finalDueDate = dueDateHistory.get(dueDateHistory.size() - 1).getDueDate();

                            differenceFirstAndLastDueDate = ChronoUnit.DAYS.between(firstDueDate, finalDueDate);
                        }
                    }

                    Long timeInImpediment = countTimeInImpediment(board, changelogItems, changelog, endDate, holidays);

                    String priority = null;
                    if (fields.has("priority") && !fields.get("priority").isJsonNull() && fields.get("priority").isJsonObject()) {
                        JsonObject priorityObj = fields.getAsJsonObject("priority");
                        priority = getAsStringSafe(priorityObj.get("name"));
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
                            .differenceFirstAndLastDueDate(differenceFirstAndLastDueDate)
                            .dueDateHistory(dueDateHistory)
                            .impedimentTime(timeInImpediment)
                            .priority(priority)
                            .build();

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    @SneakyThrows
    List<Changelog> parseChangelog(final List<JiraChangelogItem> changelogItems,
                                           final List<String> holidays, final Boolean ignoreWeekend) {
        List<Changelog> collect = changelogItems.stream()
                .filter(i -> "status".equals(i.getField()))
                .map(i -> Changelog.builder()
                        .from(i.getFromString())
                        .to(i.getToString())
                        .created(i.getCreated())
                        .build())
                .collect(Collectors.toList());

        for (int i = 0; i < collect.size(); i++) {
            Changelog current = collect.get(i);
            if (i + 1 == collect.size()) {
                current.setLeadTime(0L);
                current.setEndDate(current.getCreated());
                break;
            }

            Changelog next = collect.get(i + 1);
            current.setLeadTime(DateUtil.daysDiff(current.getCreated(), next.getCreated(), holidays, ignoreWeekend));
            current.setEndDate(next.getCreated());
        }

        return collect;
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

    private List<DueDateHistory> parseDueDateHistory(final List<JiraChangelogItem> changelogItems) {
        return changelogItems.stream()
                .filter(i -> "duedate".equals(i.getField()) && !StringUtils.isEmpty(i.getTo()))
                .map(i -> DueDateHistory.builder()
                        .created(i.getCreated())
                        .dueDate(LocalDate.parse(i.getTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .build())
                .sorted(Comparator.comparing(DueDateHistory::getCreated))
                .collect(Collectors.toList());
    }

    Long countTimeInImpediment(final Board board, final List<JiraChangelogItem> changelogItems,
                                       final List<Changelog> changelog, final LocalDateTime endDate, final List<String> holidays) {
        Long timeInImpediment;
        if (ImpedimentType.FLAG.equals(board.getImpedimentType())) {
            timeInImpediment = countTimeInImpedimentByFlag(changelogItems, endDate, holidays, board.getIgnoreWeekend());
        } else if (ImpedimentType.COLUMN.equals(board.getImpedimentType())) {
            List<String> impedimentColumns = board.getImpedimentColumns();
            timeInImpediment = impedimentColumns == null ? 0L : countTimeInImpedimentByColumn(changelog, impedimentColumns);
        } else {
            timeInImpediment = 0L;
        }

        return timeInImpediment;
    }

    private Long countTimeInImpedimentByFlag(final List<JiraChangelogItem> changelogItems, final LocalDateTime endDate,
                                             final List<String> holidays, final Boolean ignoreWeekend) {
        List<LocalDateTime> beginnings = new ArrayList<>();
        List<LocalDateTime> terms = new ArrayList<>();

        changelogItems.stream()
                .filter(i -> "flagged".equalsIgnoreCase(i.getField()))
                .forEach(i -> {
                    if ("impediment".equalsIgnoreCase(i.getToString())) {
                        beginnings.add(i.getCreated());
                    } else {
                        terms.add(i.getCreated());
                    }
                });

        if (beginnings.size() == terms.size() - 1) {
            terms.add(endDate);
        }

        if (beginnings.size() != terms.size()) {
            log.info("Method=countTimeInImpedimentByFlag, Info=tamanhos diferentes, beginnings={}, terms={}", beginnings.size(), terms.size());
            return 0L;
        }

        beginnings.sort(LocalDateTime::compareTo);
        terms.sort(LocalDateTime::compareTo);

        Long timeInImpediment = 0L;
        for (int i = 0; i < terms.size(); i++) {
            timeInImpediment += DateUtil.daysDiff(beginnings.get(i), terms.get(i), holidays, ignoreWeekend);
        }

        return timeInImpediment;
    }

    private Long countTimeInImpedimentByColumn(final List<Changelog> changelog, final List<String> impedimentColumns) {
        return changelog.stream()
                .filter(cl -> impedimentColumns.contains(cl.getTo()) && cl.getLeadTime() != null)
                .mapToLong(Changelog::getLeadTime)
                .sum();
    }

}
