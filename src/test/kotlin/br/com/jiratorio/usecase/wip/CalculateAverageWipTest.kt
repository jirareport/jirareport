package br.com.jiratorio.usecase.wip

import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class CalculateAverageWipTest {

    private val calculateAverageWip = CalculateAverageWip()

    @Test
    fun `test calc avg wip`() {
        val board = createBoard()
        val issues = createIssues(board)
        val startDate = "01/01/2019".toLocalDate()
        val endDate = "28/02/2019".toLocalDate()
        val fluxColumn = FluxColumn(board)

        val wipAvg = calculateAverageWip.execute(startDate, endDate, issues, fluxColumn.wipColumns)

        Assertions.assertThat(wipAvg)
            .isEqualTo(2.0508474576271185)
    }

    @Test
    fun `test calc avg wip with empty issues`() {
        val board = createBoard()
        val issues = emptyList<Issue>()
        val startDate = "01/01/2019".toLocalDate()
        val endDate = "28/02/2019".toLocalDate()
        val fluxColumn = FluxColumn(board)

        val wipAvg = calculateAverageWip.execute(startDate, endDate, issues, fluxColumn.wipColumns)

        Assertions.assertThat(wipAvg)
            .isZero()
    }

    private fun createIssues(board: Board): List<Issue> {
        return listOf(
            Issue(
                id = 1L,
                key = "JIRAT-1",
                board = board,
                changelog = listOf(
                    Changelog(
                        to = "BACKLOG",
                        created = "01/01/2019 12:00".toLocalDateTime(),
                        endDate = "09/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ANALYSIS",
                        created = "09/01/2019 12:00".toLocalDateTime(),
                        endDate = "15/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV WIP",
                        created = "15/01/2019 12:00".toLocalDateTime(),
                        endDate = "22/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV DONE",
                        created = "22/01/2019 12:00".toLocalDateTime(),
                        endDate = "29/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST WIP",
                        created = "29/01/2019 12:00".toLocalDateTime(),
                        endDate = "07/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST DONE",
                        created = "07/02/2019 12:00".toLocalDateTime(),
                        endDate = "10/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "REVIEW",
                        created = "10/02/2019 12:00".toLocalDateTime(),
                        endDate = "16/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DELIVERY LINE",
                        created = "16/02/2019 12:00".toLocalDateTime(),
                        endDate = "23/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ACCOMPANIMENT",
                        created = "23/02/2019 12:00".toLocalDateTime(),
                        endDate = "01/03/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DONE",
                        created = "01/03/2019 12:00".toLocalDateTime(),
                        endDate = "01/03/2019 12:00".toLocalDateTime()
                    )
                ),
                created = "01/01/2019 12:00".toLocalDateTime(),
                startDate = "09/01/2019 12:00".toLocalDateTime(),
                endDate = "23/02/2019 12:00".toLocalDateTime(),
                leadTime = 46,
                summary = "JIRAT-2 summary"
            ),
            Issue(
                id = 2L,
                key = "JIRAT-2",
                board = board,
                changelog = listOf(
                    Changelog(
                        to = "BACKLOG",
                        created = "01/01/2019 12:00".toLocalDateTime(),
                        endDate = "06/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ANALYSIS",
                        created = "06/01/2019 12:00".toLocalDateTime(),
                        endDate = "09/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV WIP",
                        created = "09/01/2019 12:00".toLocalDateTime(),
                        endDate = "18/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV DONE",
                        created = "18/01/2019 12:00".toLocalDateTime(),
                        endDate = "24/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST WIP",
                        created = "24/01/2019 12:00".toLocalDateTime(),
                        endDate = "24/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST DONE",
                        created = "25/01/2019 12:00".toLocalDateTime(),
                        endDate = "26/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "REVIEW",
                        created = "26/01/2019 12:00".toLocalDateTime(),
                        endDate = "04/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DELIVERY LINE",
                        created = "04/02/2019 12:00".toLocalDateTime(),
                        endDate = "09/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ACCOMPANIMENT",
                        created = "09/02/2019 12:00".toLocalDateTime(),
                        endDate = "15/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DONE",
                        created = "15/02/2019 12:00".toLocalDateTime(),
                        endDate = "15/02/2019 12:00".toLocalDateTime()
                    )
                ),
                created = "01/01/2019 12:00".toLocalDateTime(),
                startDate = "06/01/2019 12:00".toLocalDateTime(),
                endDate = "09/02/2019 12:00".toLocalDateTime(),
                leadTime = 35,
                summary = "JIRAT-3 summary"
            ),
            Issue(
                id = 3L,
                key = "JIRAT-3",
                board = board,
                changelog = listOf(
                    Changelog(
                        to = "BACKLOG",
                        created = "01/01/2019 12:00".toLocalDateTime(),
                        endDate = "08/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ANALYSIS",
                        created = "08/01/2019 12:00".toLocalDateTime(),
                        endDate = "13/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV WIP",
                        created = "13/01/2019 12:00".toLocalDateTime(),
                        endDate = "20/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DEV DONE",
                        created = "20/01/2019 12:00".toLocalDateTime(),
                        endDate = "24/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST WIP",
                        created = "24/01/2019 12:00".toLocalDateTime(),
                        endDate = "31/01/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "TEST DONE",
                        created = "31/01/2019 12:00".toLocalDateTime(),
                        endDate = "08/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "REVIEW",
                        created = "08/02/2019 12:00".toLocalDateTime(),
                        endDate = "11/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DELIVERY LINE",
                        created = "11/02/2019 12:00".toLocalDateTime(),
                        endDate = "16/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "ACCOMPANIMENT",
                        created = "16/02/2019 12:00".toLocalDateTime(),
                        endDate = "23/02/2019 12:00".toLocalDateTime()
                    ),
                    Changelog(
                        to = "DONE",
                        created = "23/02/2019 12:00".toLocalDateTime(),
                        endDate = "23/02/2019 12:00".toLocalDateTime()
                    )
                ),
                created = "01/01/2019 12:00".toLocalDateTime(),
                startDate = "08/01/2019 12:00".toLocalDateTime(),
                endDate = "16/02/2019 12:00".toLocalDateTime(),
                leadTime = 40,
                summary = "JIRAT-4 summary"
            )
        )
    }

    private fun createBoard(): Board {
        return Board(
            id = 1L,
            externalId = 123L,
            name = "My Board",
            startColumn = "ANALYSIS",
            endColumn = "ACCOMPANIMENT",
            fluxColumn = mutableListOf(
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
            ignoreWeekend = true
        )
    }

}
