package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.DueDateType;
import br.com.leonardoferreira.jirareport.domain.embedded.DueDateHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import java.time.LocalDateTime;
import java.util.List;

public interface DueDateService {

    List<DueDateHistory> extractDueDateHistory(String dueDateCF, List<JiraChangelogItem> changelogItems);

    Long calcDeviationOfEstimate(List<DueDateHistory> dueDateHistories, LocalDateTime endDate, DueDateType dueDateType);

}
