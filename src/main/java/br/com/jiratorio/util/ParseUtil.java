package br.com.jiratorio.util;

import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import br.com.jiratorio.domain.entity.embedded.Changelog;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
                .sorted(Comparator.comparing(Changelog::getCreated))
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

}
