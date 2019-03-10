package br.com.jiratorio.domain;

import br.com.jiratorio.context.UnitTestContext;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("unit")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestContext.class)
class FluxColumnTest {

    private FluxColumn fluxColumn;

    @Nested
    class DefaultFluxColumn {

        @BeforeEach
        void setUp() {
            List<String> orderedColumns = Arrays.asList("first_column", "second_column", "start_lead_time_column",
                    "middle_column", "second_middle_column", "end_lead_time_column", "last_column");
            fluxColumn = new FluxColumn("start_lead_time_column", "end_lead_time_column", orderedColumns);
        }

        @Test
        void testStartColumns() {
            Assertions.assertThat(fluxColumn.getStartColumns())
                    .containsExactlyInAnyOrder("start_lead_time_column", "middle_column", "second_middle_column", "end_lead_time_column");
        }

        @Test
        void testEndColumns() {
            Assertions.assertThat(fluxColumn.getEndColumns())
                    .containsExactlyInAnyOrder("end_lead_time_column", "last_column");
        }

        @Test
        void testWipColumns() {
            Assertions.assertThat(fluxColumn.getWipColumns())
                    .containsExactlyInAnyOrder("start_lead_time_column", "middle_column", "second_middle_column");
        }

        @Test
        void testLastColumn() {
            Assertions.assertThat(fluxColumn.getLastColumn())
                    .isEqualTo("last_column");
        }

    }

    @Nested
    class NullOrderedColumns {

        @BeforeEach
        void setUp() {
            fluxColumn = new FluxColumn("start_lead_time_column", "end_lead_time_column", null);
        }

        @Test
        void testStartColumns() {
            Assertions.assertThat(fluxColumn.getStartColumns())
                    .containsExactlyInAnyOrder("start_lead_time_column");
        }

        @Test
        void testEndColumns() {
            Assertions.assertThat(fluxColumn.getEndColumns())
                    .containsExactlyInAnyOrder("end_lead_time_column");
        }

        @Test
        void testWipColumns() {
            Assertions.assertThat(fluxColumn.getWipColumns())
                    .containsExactlyInAnyOrder("start_lead_time_column");
        }

        @Test
        void testLastColumn() {
            Assertions.assertThat(fluxColumn.getLastColumn())
                    .isEqualTo("Done");
        }

    }
}
