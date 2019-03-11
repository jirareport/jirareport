package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import br.com.jiratorio.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImpedimentCalculatorByFlag {

    public Long timeInImpediment(final List<JiraChangelogItem> changelogItems, final LocalDateTime endDate,
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

        if (beginnings.size() - 1 == terms.size()) {
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

}
