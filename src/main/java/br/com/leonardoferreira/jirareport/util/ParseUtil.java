package br.com.leonardoferreira.jirareport.util;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.vo.changelog.JiraChangelogItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParseUtil {

    @SneakyThrows
    public static List<Changelog> parseChangelog(final List<JiraChangelogItem> changelogItems,
                                                 final List<LocalDate> holidays, final Boolean ignoreWeekend) {
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

    public static Long countTimeInImpediment(final Board board, final List<JiraChangelogItem> changelogItems,
                                             final List<Changelog> changelog, final LocalDateTime endDate, final List<LocalDate> holidays) {
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

    @SneakyThrows
    public static List<String> toStringArray(final ObjectMapper objectMapper, final String array) {
        return objectMapper.readValue(array, new TypeReference<List<String>>() { });
    }

    private static Long countTimeInImpedimentByFlag(final List<JiraChangelogItem> changelogItems, final LocalDateTime endDate,
                                                    final List<LocalDate> holidays, final Boolean ignoreWeekend) {
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

    private static Long countTimeInImpedimentByColumn(final List<Changelog> changelog, final List<String> impedimentColumns) {
        return changelog.stream()
                .filter(cl -> impedimentColumns.contains(cl.getTo()) && cl.getLeadTime() != null)
                .mapToLong(Changelog::getLeadTime)
                .sum();
    }

}
