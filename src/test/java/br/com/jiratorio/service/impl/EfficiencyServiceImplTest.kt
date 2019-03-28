package br.com.jiratorio.service.impl

import br.com.jiratorio.assert.EfficiencyAssert
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.toLocalDateTime
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class EfficiencyServiceImplTest {

    private val efficiencyServiceImpl = EfficiencyServiceImpl()

    @Test
    fun `test calc efficiency`() {
        val changelog = defaultChangelog()
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = efficiencyServiceImpl.calcEfficiency(
            changelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        EfficiencyAssert(result).assertThat {
            hasWaitTime(2882)
            hasTouchTime(67622)
            hasPctEfficiency(95.91228866447294)
        }
    }

    @Test
    fun `test calc efficiency with empty changelog`() {
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = efficiencyServiceImpl.calcEfficiency(
            emptyList(),
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        EfficiencyAssert(result).assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    @Test
    fun `test calc efficiency with empty touching columns`() {
        val changelog = defaultChangelog()
        val touchingColumns = mutableListOf<String>()
        val waitingColumns = mutableListOf("COLUMN_WAIT_1", "COLUMN_WAIT_2", "COLUMN_WAIT_3")

        val result = efficiencyServiceImpl.calcEfficiency(
            changelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        EfficiencyAssert(result).assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    @Test
    fun `test calc efficiency with empty waiting columns`() {
        val changelog = defaultChangelog()
        val touchingColumns = mutableListOf("COLUMN_WIP_1", "COLUMN_WIP_2", "COLUMN_WIP_3")
        val waitingColumns = mutableListOf<String>()

        val result = efficiencyServiceImpl.calcEfficiency(
            changelog,
            touchingColumns,
            waitingColumns,
            emptyList(),
            null
        )

        EfficiencyAssert(result).assertThat {
            hasWaitTime(0)
            hasTouchTime(0)
            hasPctEfficiency(0.0)
        }
    }

    private fun defaultChangelog(): List<Changelog> {
        return listOf(
            Changelog(
                to = "COLUMN_WIP_1",
                created = "01/01/2019 12:00".toLocalDateTime(),
                endDate = "03/03/2019 13:00".toLocalDateTime()
            ),
            Changelog(
                to = "COLUMN_WAIT_1",
                created = "03/01/2019 13:00".toLocalDateTime(),
                endDate = "05/01/2019 11:00".toLocalDateTime()
            ),
            Changelog(
                to = "COLUMN_WIP_2",
                created = "05/01/2019 11:00".toLocalDateTime(),
                endDate = "07/01/2019 14:00".toLocalDateTime()
            ),
            Changelog(
                to = "COLUMN_WAIT_2",
                created = "07/01/2019 14:00".toLocalDateTime(),
                endDate = "07/01/2019 15:00".toLocalDateTime()
            ),
            Changelog(
                to = "COLUMN_WIP_3",
                created = "07/01/2019 15:00".toLocalDateTime(),
                endDate = "10/01/2019 12:00".toLocalDateTime()
            ),
            Changelog(
                to = "COLUMN_WAIT_3",
                created = "10/01/2019 12:00".toLocalDateTime(),
                endDate = "11/01/2019 00:00".toLocalDateTime()
            )
        )
    }

}
