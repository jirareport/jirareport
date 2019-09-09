package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@Tag("unit")
internal class FluxColumnTest {

    @Nested
    inner class DefaultFluxColumn {
        private val fluxColumn = FluxColumn(
            startLeadTimeColumn = "START_LEAD_TIME_COLUMN",
            endLeadTimeColumn = "END_LEAD_TIME_COLUMN",
            orderedColumns = listOf(
                "FIRST_COLUMN",
                "SECOND_COLUMN",
                "START_LEAD_TIME_COLUMN",
                "MIDDLE_COLUMN",
                "SECOND_MIDDLE_COLUMN",
                "END_LEAD_TIME_COLUMN",
                "LAST_COLUMN"
            )
        )

        @Test
        fun `start columns`() {
            assertThat(fluxColumn.startColumns)
                .containsExactlyInAnyOrder(
                    "START_LEAD_TIME_COLUMN",
                    "MIDDLE_COLUMN",
                    "SECOND_MIDDLE_COLUMN",
                    "END_LEAD_TIME_COLUMN"
                )
        }

        @Test
        fun `end columns`() {
            assertThat(fluxColumn.endColumns)
                .containsExactlyInAnyOrder("END_LEAD_TIME_COLUMN", "LAST_COLUMN")
        }

        @Test
        fun `wip columns`() {
            assertThat(fluxColumn.wipColumns)
                .containsExactlyInAnyOrder("START_LEAD_TIME_COLUMN", "MIDDLE_COLUMN", "SECOND_MIDDLE_COLUMN")
        }

        @Test
        fun `last column`() {
            assertThat(fluxColumn.lastColumn)
                .isEqualTo("LAST_COLUMN")
        }

    }

    @Nested
    inner class NullOrderedColumns {
        private val fluxColumn =
            FluxColumn("START_LEAD_TIME_COLUMN", "END_LEAD_TIME_COLUMN", null)

        @Test
        fun `start columns`() {
            assertThat(fluxColumn.startColumns)
                .containsExactlyInAnyOrder("START_LEAD_TIME_COLUMN")
        }

        @Test
        fun `end columns`() {
            assertThat(fluxColumn.endColumns)
                .containsExactlyInAnyOrder("END_LEAD_TIME_COLUMN")
        }

        @Test
        fun `wip columns`() {
            assertThat(fluxColumn.wipColumns)
                .containsExactlyInAnyOrder("START_LEAD_TIME_COLUMN")
        }

        @Test
        fun `last column`() {
            assertThat(fluxColumn.lastColumn)
                .isEqualTo("DONE")
        }

    }

    @Nested
    inner class StartAndEndDateCalculator {

        @Test
        fun `test calc start and end date`() {
            val fluxColumn = FluxColumn(
                startLeadTimeColumn = "START_LEAD_TIME_COLUMN",
                endLeadTimeColumn = "END_LEAD_TIME_COLUMN",
                orderedColumns = listOf(
                    "FIRST_COLUMN",
                    "SECOND_COLUMN",
                    "START_LEAD_TIME_COLUMN",
                    "MIDDLE_COLUMN",
                    "SECOND_MIDDLE_COLUMN",
                    "END_LEAD_TIME_COLUMN",
                    "LAST_COLUMN"
                )
            )

            val changelog = listOf(
                Changelog(to = "first_column", created = "01/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "second_column", created = "09/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "start_lead_time_column", created = "13/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "middle_column", created = "18/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "second_middle_column", created = "19/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "end_lead_time_column", created = "24/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "last_column", created = "28/01/2019 12:00".toLocalDateTime())
            )

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(changelog, LocalDateTime.now())

            assertThat(startDate)
                .isEqualTo("13/01/2019 12:00".toLocalDateTime())

            assertThat(endDate)
                .isEqualTo("24/01/2019 12:00".toLocalDateTime())
        }

        @Test
        fun `test calc start and end date with backlog`() {
            val fluxColumn = FluxColumn(
                startLeadTimeColumn = "BACKLOG",
                endLeadTimeColumn = "DONE",
                orderedColumns = listOf(
                    "BACKLOG",
                    "ANALYSIS",
                    "DEV WIP",
                    "DEV DONE",
                    "TEST WIP",
                    "TEST DONE",
                    "REVIEW",
                    "DELIVERY LINE",
                    "ACCOMPANIMENT",
                    "DONE"
                )
            )

            val changelog = listOf(
                Changelog(to = "ANALYSIS", created = "05/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "DEV WIP", created = "12/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "DEV DONE", created = "13/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "TEST WIP", created = "21/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "TEST DONE", created = "28/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "REVIEW", created = "04/02/2019 12:00".toLocalDateTime()),
                Changelog(to = "DELIVERY LINE", created = "09/02/2019 12:00".toLocalDateTime()),
                Changelog(to = "ACCOMPANIMENT", created = "11/02/2019 12:00".toLocalDateTime()),
                Changelog(to = "DONE", created = "14/02/2019 12:00".toLocalDateTime())
            )

            val created = "01/01/2019 12:00".toLocalDateTime()

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(changelog, created)

            assertThat(startDate)
                .isEqualTo(created)

            assertThat(endDate)
                .isEqualTo("14/02/2019 12:00".toLocalDateTime())
        }

        @Test
        fun `test calc start and end date with not found events in changelog`() {
            val fluxColumn = FluxColumn(
                startLeadTimeColumn = "ANALYSIS",
                endLeadTimeColumn = "ACCOMPANIMENT",
                orderedColumns = listOf(
                    "BACKLOG",
                    "ANALYSIS",
                    "DEV WIP",
                    "DEV DONE",
                    "TEST WIP",
                    "TEST DONE",
                    "REVIEW",
                    "DELIVERY LINE",
                    "ACCOMPANIMENT",
                    "DONE"
                )
            )

            val changelog = listOf(
                Changelog(to = "TODO", created = "01/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "WIP", created = "10/01/2019 12:00".toLocalDateTime()),
                Changelog(to = "CLOSED", created = "12/01/2019 12:00".toLocalDateTime())
            )

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(changelog, LocalDateTime.now())

            assertThat(startDate)
                .isNull()

            assertThat(endDate)
                .isNull()
        }
    }
}
