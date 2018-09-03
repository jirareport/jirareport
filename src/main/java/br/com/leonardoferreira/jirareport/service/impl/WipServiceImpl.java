package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.service.WipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
@Service
public class WipServiceImpl implements WipService {

    @Override
    public Double calcAvgWip(final LocalDate start, final LocalDate end,
                           final List<Issue> issues, final Set<String> wipColumns) {
        log.info("Method=calcAvgWip, start={}, end={}, issues={}", start, end, issues);

        Map<String, Long> dailyWip = new TreeMap<>();

        LocalDate aux = start;
        while (aux.isBefore(end) || aux.equals(end)) {
            LocalDate currentDate = aux;
            long issuesInWipColumn = issues.stream()
                    .map(i -> i.getChangelog().stream()
                            .filter(cl -> cl.getEndDate() != null && cl.getCreated() != null)
                            .filter(cl -> currentDate.isBefore(cl.getEndDate().toLocalDate())
                                    && currentDate.isAfter(cl.getCreated().toLocalDate())
                                    || currentDate.equals(cl.getCreated().toLocalDate()))
                            .filter(cl -> wipColumns.contains(cl.getTo()))
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .count();

            dailyWip.put(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), issuesInWipColumn);
            aux = currentDate.plusDays(1);
        }

        if (dailyWip.isEmpty()) {
            return 0D;
        }

        long sum = dailyWip.values().stream()
                    .mapToLong(i -> i)
                    .sum();

        return (double) sum / dailyWip.size();
    }

}
