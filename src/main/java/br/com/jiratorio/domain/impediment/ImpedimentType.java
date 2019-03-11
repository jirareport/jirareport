package br.com.jiratorio.domain.impediment;

import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import br.com.jiratorio.domain.entity.embedded.Changelog;
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByColumn;
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByFlag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public enum ImpedimentType {

    COLUMN {
        private ImpedimentCalculatorByColumn calculator = new ImpedimentCalculatorByColumn();

        @Override
        public Long timeInImpediment(final List<String> impedimentColumns, final List<JiraChangelogItem> changelogItems,
                                     final List<Changelog> changelog, final LocalDateTime endDate, final List<LocalDate> holidays,
                                     final Boolean ignoreWeekend) {
            return calculator.timeInImpediment(changelog, impedimentColumns);
        }
    },

    FLAG {
        private ImpedimentCalculatorByFlag calculator = new ImpedimentCalculatorByFlag();

        @Override
        public Long timeInImpediment(final List<String> impedimentColumns, final List<JiraChangelogItem> changelogItems,
                                     final List<Changelog> changelog, final LocalDateTime endDate, final List<LocalDate> holidays,
                                     final Boolean ignoreWeekend) {

            return calculator.timeInImpediment(changelogItems, endDate, holidays, ignoreWeekend);
        }
    };

    public abstract Long timeInImpediment(List<String> impedimentColumns, List<JiraChangelogItem> changelogItems,
                                          List<Changelog> changelog, LocalDateTime endDate, List<LocalDate> holidays,
                                          Boolean ignoreWeekend);

}
