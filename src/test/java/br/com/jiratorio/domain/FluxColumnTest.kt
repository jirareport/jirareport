package br.com.jiratorio.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.Arrays.asList

@Tag("unit")
internal class FluxColumnTest {

    @Nested
    inner class DefaultFluxColumn {
        private val fluxColumn = FluxColumn(
            "start_lead_time_column", "end_lead_time_column",
            asList(
                "first_column",
                "second_column",
                "start_lead_time_column",
                "middle_column",
                "second_middle_column",
                "end_lead_time_column",
                "last_column"
            )
        )

        @Test
        fun `start columns`() {
            assertThat(fluxColumn.startColumns)
                .containsExactlyInAnyOrder(
                    "start_lead_time_column",
                    "middle_column",
                    "second_middle_column",
                    "end_lead_time_column"
                )
        }

        @Test
        fun `end columns`() {
            assertThat(fluxColumn.endColumns)
                .containsExactlyInAnyOrder("end_lead_time_column", "last_column")
        }

        @Test
        fun `wip columns`() {
            assertThat(fluxColumn.wipColumns)
                .containsExactlyInAnyOrder("start_lead_time_column", "middle_column", "second_middle_column")
        }

        @Test
        fun `last column`() {
            assertThat(fluxColumn.lastColumn)
                .isEqualTo("last_column")
        }

    }

    @Nested
    inner class NullOrderedColumns {
        private val fluxColumn =
            FluxColumn("start_lead_time_column", "end_lead_time_column", null)

        @Test
        fun `start columns`() {
            assertThat(fluxColumn.startColumns)
                .containsExactlyInAnyOrder("start_lead_time_column")
        }

        @Test
        fun `end columns`() {
            assertThat(fluxColumn.endColumns)
                .containsExactlyInAnyOrder("end_lead_time_column")
        }

        @Test
        fun `wip columns`() {
            assertThat(fluxColumn.wipColumns)
                .containsExactlyInAnyOrder("start_lead_time_column")
        }

        @Test
        fun `last column`() {
            assertThat(fluxColumn.lastColumn)
                .isEqualTo("Done")
        }

    }

}
