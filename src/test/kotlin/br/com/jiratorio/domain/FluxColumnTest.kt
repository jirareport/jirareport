package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
class FluxColumnTest {

    @Nested
    inner class DefaultFluxColumn {
        private val fluxColumn = createDefaultFluxColumn(
            endLeadTimeColumn = "ACCOMPANIMENT"
        )

        @Test
        fun `start columns`() {
            assertThat(fluxColumn.startColumns)
                .containsExactlyInAnyOrder(
                    "ANALYSIS",
                    "DEV WIP",
                    "DEV DONE",
                    "TEST WIP",
                    "TEST DONE",
                    "REVIEW",
                    "DELIVERY LINE",
                    "ACCOMPANIMENT"
                )
        }

        @Test
        fun `end columns`() {
            assertThat(fluxColumn.endColumns)
                .containsExactlyInAnyOrder("ACCOMPANIMENT", "DONE")
        }

        @Test
        fun `wip columns`() {
            assertThat(fluxColumn.wipColumns)
                .containsExactlyInAnyOrder(
                    "ANALYSIS",
                    "DEV WIP",
                    "DEV DONE",
                    "TEST WIP",
                    "TEST DONE",
                    "REVIEW",
                    "DELIVERY LINE"
                )
        }

        @Test
        fun `last column`() {
            assertThat(fluxColumn.lastColumn)
                .isEqualTo("DONE")
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
            val fluxColumn = createDefaultFluxColumn(
                startLeadTimeColumn = "DEV WIP",
                endLeadTimeColumn = "ACCOMPANIMENT"
            )

            val columnChangelog = setOf(
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "01/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV WIP",
                    startDate = "09/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV DONE",
                    startDate = "13/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST WIP",
                    startDate = "18/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST DONE",
                    startDate = "19/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "REVIEW",
                    startDate = "24/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DELIVERY LINE",
                    startDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ACCOMPANIMENT",
                    startDate = "29/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DONE",
                    startDate = "30/01/2019 12:00".toLocalDateTime()
                )
            )

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(columnChangelog, LocalDateTime.now())

            assertThat(startDate)
                .isEqualTo("09/01/2019 12:00".toLocalDateTime())

            assertThat(endDate)
                .isEqualTo("29/01/2019 12:00".toLocalDateTime())
        }

        @Test
        fun `test calc start and end date with backlog`() {
            val fluxColumn = createDefaultFluxColumn(startLeadTimeColumn = "BACKLOG")

            val columnChangelog = setOf(
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "05/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV WIP",
                    startDate = "12/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV DONE",
                    startDate = "13/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST WIP",
                    startDate = "21/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST DONE",
                    startDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "REVIEW",
                    startDate = "04/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DELIVERY LINE",
                    startDate = "09/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ACCOMPANIMENT",
                    startDate = "11/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DONE",
                    startDate = "14/02/2019 12:00".toLocalDateTime()
                )
            )

            val created = "01/01/2019 12:00".toLocalDateTime()

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(columnChangelog, created)

            assertThat(startDate)
                .isEqualTo(created)

            assertThat(endDate)
                .isEqualTo("14/02/2019 12:00".toLocalDateTime())
        }

        @Test
        fun `test calc start and end date with not found events in column changelog`() {
            val fluxColumn = createDefaultFluxColumn(endLeadTimeColumn = "ACCOMPANIMENT")

            val columnChangelog = setOf(
                ColumnChangelogEntity(
                    to = "TODO",
                    startDate = "01/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "WIP",
                    startDate = "10/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "CLOSED",
                    startDate = "12/01/2019 12:00".toLocalDateTime()
                )
            )

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(columnChangelog, LocalDateTime.now())

            assertThat(startDate)
                .isNull()

            assertThat(endDate)
                .isNull()
        }
    }

    @Nested
    inner class CalculatorWithDevaluation {

        @Test
        fun `use last occurrence of start column`() {
            val fluxColumn = createDefaultFluxColumn(
                useLastOccurrenceWhenCalculateLeadTime = true
            )

            val columnChangelog = setOf(
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "05/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "BACKLOG",
                    startDate = "06/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "10/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV WIP",
                    startDate = "12/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV DONE",
                    startDate = "13/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST WIP",
                    startDate = "21/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST DONE",
                    startDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "REVIEW",
                    startDate = "04/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DELIVERY LINE",
                    startDate = "09/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ACCOMPANIMENT",
                    startDate = "11/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DONE",
                    startDate = "14/02/2019 12:00".toLocalDateTime()
                )
            )

            val created = "01/01/2019 12:00".toLocalDateTime()

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(columnChangelog, created)

            assertThat(startDate)
                .isEqualTo("10/01/2019 12:00".toLocalDateTime())

            assertThat(endDate)
                .isEqualTo("14/02/2019 12:00".toLocalDateTime())
        }

        @Test
        fun `use last occurrence of backlog column`() {
            val fluxColumn = createDefaultFluxColumn(
                startLeadTimeColumn = "BACKLOG",
                useLastOccurrenceWhenCalculateLeadTime = true
            )

            val columnChangelog = setOf(
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "05/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "BACKLOG",
                    startDate = "06/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ANALYSIS",
                    startDate = "10/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV WIP",
                    startDate = "12/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DEV DONE",
                    startDate = "13/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST WIP",
                    startDate = "21/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "TEST DONE",
                    startDate = "28/01/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "REVIEW",
                    startDate = "04/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DELIVERY LINE",
                    startDate = "09/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "ACCOMPANIMENT",
                    startDate = "11/02/2019 12:00".toLocalDateTime()
                ),
                ColumnChangelogEntity(
                    to = "DONE",
                    startDate = "14/02/2019 12:00".toLocalDateTime()
                )
            )

            val created = "01/01/2019 12:00".toLocalDateTime()

            val (
                startDate,
                endDate
            ) = fluxColumn.calcStartAndEndDate(columnChangelog, created)

            assertThat(startDate)
                .isEqualTo("06/01/2019 12:00".toLocalDateTime())

            assertThat(endDate)
                .isEqualTo("14/02/2019 12:00".toLocalDateTime())
        }

    }

    private fun createDefaultFluxColumn(
        startLeadTimeColumn: String = "ANALYSIS",
        endLeadTimeColumn: String = "DONE",
        orderedColumns: List<String> = listOf(
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
        ),
        useLastOccurrenceWhenCalculateLeadTime: Boolean = false
    ) = FluxColumn(
        startLeadTimeColumn = startLeadTimeColumn,
        endLeadTimeColumn = endLeadTimeColumn,
        orderedColumns = orderedColumns,
        useLastOccurrenceWhenCalculateLeadTime = useLastOccurrenceWhenCalculateLeadTime
    )
}
