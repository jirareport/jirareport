package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.DueDateType;
import br.com.jiratorio.domain.embedded.DueDateHistory;
import br.com.jiratorio.domain.vo.changelog.JiraChangelogItem;
import br.com.jiratorio.service.DueDateService;
import br.com.jiratorio.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                        .dueDate(parseDueDate(i.getTo()))
                        .build())
                .sorted(Comparator.comparing(DueDateHistory::getCreated))
                .collect(Collectors.toList());
    }

    @Override
    public Long calcDeviationOfEstimate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate,
                                        final DueDateType dueDateType, final Boolean ignoreWeekend, final List<LocalDate> holidays) {
        log.info("Method=calcDeviationOfEstimate, dueDateHistories={}, endDate={}, dueDateType={}, ignoreWeekend={}, holidays={}",
                dueDateHistories, endDate, dueDateType, ignoreWeekend, holidays);

        if (CollectionUtils.isEmpty(dueDateHistories) || dueDateType == null) {
            return null;
        }

        switch (dueDateType) {
            case FIRST_AND_LAST_DUE_DATE:
                return calcDeviationOfEstimateBetweenFirstAndLastDueDate(dueDateHistories, ignoreWeekend, holidays);
            case FIRST_DUE_DATE_AND_END_DATE:
                return calcDeviationOfEstimateBetweenFirstDueDateAndEndDate(dueDateHistories, endDate, ignoreWeekend, holidays);
            case LAST_DUE_DATE_AND_END_DATE:
                return calcDeviationOfEstimateBetweenLastDueDateAndEndDate(dueDateHistories, endDate, ignoreWeekend, holidays);
            default:
                return null;
        }
    }

    private Long calcDeviationOfEstimateBetweenFirstAndLastDueDate(final List<DueDateHistory> dueDateHistories,
                                                                   final Boolean ignoreWeekend, final List<LocalDate> holidays) {
        log.info("Method=calcDeviationOfEstimateBetweenFirstAndLastDueDate, dueDateHistories={}, ignoreWeekend={}, holidays={}",
                dueDateHistories, ignoreWeekend, holidays);

        DueDateHistory first = dueDateHistories.get(0);
        DueDateHistory last = dueDateHistories.get(dueDateHistories.size() - 1);

        return DateUtil.daysDiff(first.getDueDate(), last.getDueDate(), holidays, ignoreWeekend);
    }

    private Long calcDeviationOfEstimateBetweenFirstDueDateAndEndDate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate,
                                                                      final Boolean ignoreWeekend, final List<LocalDate> holidays) {
        log.info("Method=calcDeviationOfEstimateBetweenFirstDueDateAndEndDate, dueDateHistories={}, endDate={}, ignoreWeekend={}, holidays={}",
                dueDateHistories, endDate, ignoreWeekend, holidays);

        DueDateHistory first = dueDateHistories.get(0);
        return DateUtil.daysDiff(first.getDueDate(), endDate.toLocalDate(), holidays, ignoreWeekend);
    }

    private Long calcDeviationOfEstimateBetweenLastDueDateAndEndDate(final List<DueDateHistory> dueDateHistories, final LocalDateTime endDate,
                                                                     final Boolean ignoreWeekend, final List<LocalDate> holidays) {
        log.info("Method=calcDeviationOfEstimateBetweenLastDueDateAndEndDate, dueDateHistories={}, endDate={}",
                dueDateHistories, endDate);

        DueDateHistory last = dueDateHistories.get(dueDateHistories.size() - 1);
        return DateUtil.daysDiff(last.getDueDate(), endDate.toLocalDate(), holidays, ignoreWeekend);
    }

    private LocalDate parseDueDate(final String dueDateStr) {
        if (StringUtils.isEmpty(dueDateStr)) {
            return null;
        }

        if (dueDateStr.length() > 19) {
            LocalDateTime localDateTime = DateUtil.parseFromJira(dueDateStr);
            return localDateTime.toLocalDate();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][dd/MM/yyyy]");
        return LocalDate.parse(dueDateStr, formatter);
    }

}
