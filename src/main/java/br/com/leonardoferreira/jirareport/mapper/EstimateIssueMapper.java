package br.com.leonardoferreira.jirareport.mapper;


import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelog;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class EstimateIssueMapper {
    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IssueMapper issueMapper;

    @SneakyThrows
    public List<EstimateIssue> parseEstimate(final String rawText, final Board board) {
        JsonNode jsonNode = objectMapper.readTree(rawText);
        Iterator<JsonNode> issues = jsonNode.path("issues").elements();

        final List<String> holidays = holidayService.findByBoard(board.getId())
                .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        Set<String> startColumns = CalcUtil.calcStartColumns(board);

        Iterable<JsonNode> iterable = ()->issues;
        List<EstimateIssue> list = StreamSupport.stream(iterable.spliterator(), false)
                .map(issue -> {
                    JsonNode fields = issue.path("fields");

                    List<JiraChangelogItem> changelogItems = extractChangelogItems(issue);
                    List<Changelog> changelog = issueMapper.parseChangelog(changelogItems, holidays, board.getIgnoreWeekend());

                    LocalDateTime created = DateUtil.parseFromJira(fields.get("created").asText());

                    LocalDateTime startDate = null;

                    for (Changelog cl : changelog) {
                        if (startDate == null && startColumns.contains(cl.getTo())) {
                            startDate = cl.getCreated();
                        }
                    }
//                    if ("BACKLOG".equals(board.getStartColumn())) {
//                        startDate = created;
//                    }


                    String priority = null;
                    if (fields.has("priority") && !fields.get("priority").isNull()) {
                        priority = fields.path("priority").asText(null);
                    }


//                    Long leadTime = DateUtil.daysDiff(startDate, endDate, holidays, board.getIgnoreWeekend());
                    Long leadTime = DateUtil.daysDiff(startDate, LocalDateTime.now(), new ArrayList<>(), false);

                    String author = null;
                    JsonNode creator = fields.path("creator");
                    if (creator != null) {
                        author = creator.get("displayName").asText(null);
                    }
//                    return EstimateIssue.builder()
//                            .creator(author)
//                            .key(issue.get("key").asText(null))
//                            .issueType(fields.path("issuetype").get("name").asText(null))
//                            .created(created)
//                            .startDate(startDate)
//                            .leadTime(leadTime)
//                            .system(parseElement(fields, board.getSystemCF()))
//                            .epic(parseElement(fields, board.getEpicCF()))
//                            .estimated(parseElement(fields, board.getEstimateCF()))
//                            .project(parseElement(fields, board.getProjectCF()))
//                            .summary(fields.get("summary").asText())
//                            .changelog(changelog)
//                            .board(board)
//                            .differenceFirstAndLastDueDate(differenceFirstAndLastDueDate)
//                            .dueDateHistory(dueDateHistory)
//                            .impedimentTime(timeInImpediment)
//                            .priority(priority)
//                            .build();
                    return EstimateIssue.builder()
                            .creator(author)
                            .key(issue.get("key").asText(null))
                            .issueType(fields.path("issuetype").get("name").asText(null))
                            .created(created)
                            .startDate(startDate)
                            .leadTime(leadTime)
                            .summary(fields.get("summary").asText())
                            .changelog(changelog)
                            .board(board)
                            .priority(priority)
                            .build();
                }).collect(Collectors.toList());
        return list;
    }


    @SneakyThrows
    private List<JiraChangelogItem> extractChangelogItems(JsonNode issue) {
        JiraChangelog changelog = objectMapper.readValue(issue.path("changelog").toString(), JiraChangelog.class);
        changelog.getHistories().forEach(cl -> cl.getItems().forEach(i -> i.setCreated(cl.getCreated())));
        return changelog.getHistories().parallelStream()
                .map(JiraChangelogHistory::getItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
