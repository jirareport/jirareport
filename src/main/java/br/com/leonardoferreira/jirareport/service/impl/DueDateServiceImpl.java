package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.DueDateType;
import br.com.leonardoferreira.jirareport.domain.embedded.DueDateHistory;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import br.com.leonardoferreira.jirareport.service.DueDateService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class DueDateServiceImpl implements DueDateService {

    @Override
    public List<DueDateHistory> extractDueDateHistory(final String dueDateCF, final List<JiraChangelogItem> changelogItems) {
        log.info("Method=extractDueDateHistory, dueDateCF={}, changelogItems={}", dueDateCF, changelogItems);

        return changelogItems.stream()
                .filter(i -> dueDateCF.equals(i.getField()) && !StringUtils.isEmpty(i.getTo()))
                .map(i -> DueDateHistory.builder()
                        .created(i.getCreated())
                        .dueDate(LocalDate.parse(i.getTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .build())
                .sorted(Comparator.comparing(DueDateHistory::getCreated))
                .collect(Collectors.toList());
    }

    @Override
    public Long calcDeviationOfEstimate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate, final DueDateType dueDateType) {
        log.info("Method=calcDeviationOfEstimate, dueDateHistories={}, endDate={}, dueDateType={}", dueDateHistories, endDate, dueDateType);

        if (CollectionUtils.isEmpty(dueDateHistories) || dueDateType == null) {
            return null;
        }

        switch (dueDateType) {
            case FIRST_AND_LAST_DUE_DATE:
                return calcDeviationOfEstimateBetweenFirstAndLastDueDate(dueDateHistories);
            case FIRST_DUE_DATE_AND_END_DATE:
                return calcDeviationOfEstimateBetweenFirstDueDateAndEndDate(dueDateHistories, endDate);
            case LAST_DUE_DATE_AND_END_DATE:
                return calcDeviationOfEstimateBetweenLastDueDateAndEndDate(dueDateHistories, endDate);
            default:
                return null;
        }
    }

    private Long calcDeviationOfEstimateBetweenFirstAndLastDueDate(final List<DueDateHistory> dueDateHistories) {
        log.info("Method=calcDeviationOfEstimateBetweenFirstAndLastDueDate, dueDateHistories={}", dueDateHistories);

        DueDateHistory first = dueDateHistories.get(0);
        DueDateHistory last = dueDateHistories.get(dueDateHistories.size() - 1);

        return ChronoUnit.DAYS.between(first.getDueDate(), last.getDueDate());
    }

    private Long calcDeviationOfEstimateBetweenFirstDueDateAndEndDate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate) {
        log.info("Method=calcDeviationOfEstimateBetweenFirstDueDateAndEndDate, dueDateHistories={}, endDate={}", dueDateHistories, endDate);

        DueDateHistory first = dueDateHistories.get(0);
        return ChronoUnit.DAYS.between(first.getDueDate(), endDate.toLocalDate());
    }

    private Long calcDeviationOfEstimateBetweenLastDueDateAndEndDate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate) {
        log.info("Method=calcDeviationOfEstimateBetweenLastDueDateAndEndDate, dueDateHistories={}, endDate={}", dueDateHistories, endDate);

        DueDateHistory last = dueDateHistories.get(dueDateHistories.size() - 1);
        return ChronoUnit.DAYS.between(last.getDueDate(), endDate.toLocalDate());
    }

}
