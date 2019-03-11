package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ImpedimentCalculatorByFlagTest {

    @Test
    void testTimeInImpediment() {
        List<JiraChangelogItem> changelogItems = Arrays.asList(
                new JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 1, 12, 0)),
                new JiraChangelogItem("customfield_123", null, null, null, null, null, null),
                new JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 10, 12, 0)),
                new JiraChangelogItem("xablau", null, null, null, null, null, null),
                new JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                new JiraChangelogItem("bla", null, null, null, null, null, null),
                new JiraChangelogItem("other", null, null, null, null, null, null),
                new JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 19, 12, 0))
        );
        ImpedimentCalculatorByFlag calculator = new ImpedimentCalculatorByFlag(changelogItems, LocalDateTime.now(),
                Collections.emptyList(), Boolean.TRUE);

        Assertions.assertThat(calculator.timeInImpediment())
                .isEqualTo(15);
    }

    @Test
    void testTimeInImpedimentWithoutTerm() {
        List<JiraChangelogItem> changelogItems = Arrays.asList(
                new JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 5, 12, 0)),
                new JiraChangelogItem("customfield_123", null, null, null, null, null, null),
                new JiraChangelogItem("flagged", null, null, null, null, null, LocalDateTime.of(2019, 1, 9, 12, 0)),
                new JiraChangelogItem("xablau", null, null, null, null, null, null),
                new JiraChangelogItem("flagged", null, null, null, null, "impediment", LocalDateTime.of(2019, 1, 15, 12, 0)),
                new JiraChangelogItem("bla", null, null, null, null, null, null),
                new JiraChangelogItem("other", null, null, null, null, null, null)
        );
        ImpedimentCalculatorByFlag calculator = new ImpedimentCalculatorByFlag(changelogItems, LocalDateTime.of(2019, 1, 19, 12, 0),
                Collections.emptyList(), Boolean.TRUE);

        Assertions.assertThat(calculator.timeInImpediment())
                .isEqualTo(10);
    }
}
