package br.com.jiratorio.service;

import br.com.jiratorio.domain.DueDateType;
import br.com.jiratorio.domain.embedded.DueDateHistory;
import br.com.jiratorio.domain.vo.changelog.JiraChangelogItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DueDateService {

    List<DueDateHistory> extractDueDateHistory(String dueDateCF, List<JiraChangelogItem> changelogItems);

    Long calcDeviationOfEstimate(List<DueDateHistory> dueDateHistories, LocalDateTime endDate, DueDateType dueDateType,
                                 Boolean ignoreWeekend, List<LocalDate> holidays);

}
