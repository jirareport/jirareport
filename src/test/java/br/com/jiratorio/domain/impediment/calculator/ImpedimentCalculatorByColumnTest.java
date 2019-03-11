package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.entity.embedded.Changelog;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImpedimentCalculatorByColumnTest {

    private ImpedimentCalculatorByColumn calculator;

    @BeforeEach
    void setUp() {
        calculator = new ImpedimentCalculatorByColumn();
    }

    @Test
    void testTimeInImpediment() {
        List<Changelog> changelogs = Arrays.asList(
                new Changelog(null, null, "COLUMN_ONE", 1L, null),
                new Changelog(null, null, "IMP_COLUMN_ONE", 2L, null),
                new Changelog(null, null, "COLUMN_TWO", 3L, null),
                new Changelog(null, null, "IMP_COLUMN_TWO", 4L, null),
                new Changelog(null, null, "COLUMN_THREE", 5L, null),
                new Changelog(null, null, "IMP_COLUMN_THREE", 6L, null),
                new Changelog(null, null, "COLUMN_FOUR", 7L, null)
        );
        List<String> columns = Arrays.asList("IMP_COLUMN_ONE", "IMP_COLUMN_TWO", "IMP_COLUMN_THREE");

        Assertions.assertThat(calculator.timeInImpediment(changelogs, columns))
                .isEqualTo(12);
    }

}
