package br.com.jiratorio.usecase.efficiency

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.extension.toLocalDateTime
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class CalculateEfficiencyTest {

    private val calculateEfficiency = CalculateEfficiency()

    @Test
    fun `test calc efficiency`() {
        val columnChangelog = defaultColumnChangelog()
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = calculateEfficiency.execute(
            columnChangelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        result.assertThat {
            hasWaitTime(2882)
            hasTouchTime(67622)
            hasPctEfficiency(95.91228866447294)
        }
    }

    @Test
    fun `test calc efficiency with empty column changelog`() {
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = calculateEfficiency.execute(
            emptySet(),
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        result.assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    @Test
    fun `test calc efficiency with empty touching columns`() {
        val columnChangelog = defaultColumnChangelog()
        val touchingColumns = mutableListOf<String>()
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = calculateEfficiency.execute(
            columnChangelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        result.assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    @Test
    fun `test calc efficiency with empty waiting columns`() {
        val columnChangelog = defaultColumnChangelog()
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf<String>()

        val result = calculateEfficiency.execute(
            columnChangelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        result.assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    private fun defaultColumnChangelog(): Set<ColumnChangelog> =
        setOf(
            ColumnChangelog(
                to = "COLUMN_WIP_1",
                startDate = "01/01/2019 12:00".toLocalDateTime(),
                endDate = "03/03/2019 13:00".toLocalDateTime()
            ),
            ColumnChangelog(
                to = "COLUMN_WAIT_1",
                startDate = "03/01/2019 13:00".toLocalDateTime(),
                endDate = "05/01/2019 11:00".toLocalDateTime()
            ),
            ColumnChangelog(
                to = "COLUMN_WIP_2",
                startDate = "05/01/2019 11:00".toLocalDateTime(),
                endDate = "07/01/2019 14:00".toLocalDateTime()
            ),
            ColumnChangelog(
                to = "COLUMN_WAIT_2",
                startDate = "07/01/2019 14:00".toLocalDateTime(),
                endDate = "07/01/2019 15:00".toLocalDateTime()
            ),
            ColumnChangelog(
                to = "COLUMN_WIP_3",
                startDate = "07/01/2019 15:00".toLocalDateTime(),
                endDate = "10/01/2019 12:00".toLocalDateTime()
            ),
            ColumnChangelog(
                to = "COLUMN_WAIT_3",
                startDate = "10/01/2019 12:00".toLocalDateTime(),
                endDate = "11/01/2019 00:00".toLocalDateTime()
            )
        )

}
