package br.com.jiratorio.usecase.jql

import br.com.jiratorio.config.junit.testtype.UnitTest
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.extension.toLocalDate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

@UnitTest
internal class CreateFinalizedIssueJqlTest {

    private val startDate: LocalDate = "01/01/2019".toLocalDate()

    private val endDate: LocalDate = "31/01/2019".toLocalDate()

    private val finalizedIssueJql = CreateFinalizedIssueJql()

    @Test
    fun `finalized issues with ignore issue type`() {
        val board = createBoard(mutableListOf("IT_1"))

        val jql = finalizedIssueJql.execute(board, startDate, endDate)

        Assertions.assertThat(jql).isEqualTo(
            """
               | project = '123123'
               | AND (
               |      STATUS CHANGED TO 'DONE' DURING('2019-01-01', '2019-01-31 23:59')
               |      OR (
               |          STATUS CHANGED TO 'DONE' DURING ('2019-01-01', '2019-01-31 23:59')
               |          AND NOT STATUS CHANGED TO 'DONE'
               |      )
               |      OR (
               |          resolutiondate >= '2019-01-01' AND resolutiondate <= '2019-01-31 23:59'
               |          AND NOT STATUS CHANGED TO 'DONE'
               |          AND NOT STATUS CHANGED TO 'DONE'
               |      )
               | )
               | AND issueType NOT IN ('IT_1')
               | AND status WAS IN ('TODO','WIP','DONE')
               | AND status IN ('DONE')
                """.trimMargin().replace("\n", "")
        )
    }

    @Test
    fun `finalized issues without ignore issue type`() {
        val board = createBoard()
        val jql = finalizedIssueJql.execute(board, startDate, endDate)

        Assertions.assertThat(jql).isEqualTo(
            """
               | project = '123123'
               | AND (
               |      STATUS CHANGED TO 'DONE' DURING('2019-01-01', '2019-01-31 23:59')
               |      OR (
               |          STATUS CHANGED TO 'DONE' DURING ('2019-01-01', '2019-01-31 23:59')
               |          AND NOT STATUS CHANGED TO 'DONE'
               |      )
               |      OR (
               |          resolutiondate >= '2019-01-01' AND resolutiondate <= '2019-01-31 23:59'
               |          AND NOT STATUS CHANGED TO 'DONE'
               |          AND NOT STATUS CHANGED TO 'DONE'
               |      )
               | )
               | ${""}
               | AND status WAS IN ('TODO','WIP','DONE')
               | AND status IN ('DONE')
                """.trimMargin().replace("\n", "")
        )
    }

    @Test
    fun `finalized issues with backslash`() {
        val board = createBoard(
            fluxColumn = mutableListOf("TODO", "WAITING UI\\UX", "UI\\UX DONE", "WIP", "DONE")
        )

        val jql = finalizedIssueJql.execute(board, startDate, endDate)
        Assertions.assertThat(jql)
            .contains("'WAITING UI\\\\UX'")
            .contains("'UI\\\\UX DONE'")
    }

    private fun createBoard(
        ignoreIssueType: MutableList<String>? = null,
        fluxColumn: MutableList<String> = mutableListOf("TODO", "WIP", "DONE")
    ) =
        Board(
            name = "test board",
            externalId = 123123L,
            startColumn = "TODO",
            endColumn = "DONE",
            fluxColumn = fluxColumn,
            ignoreIssueType = ignoreIssueType
        )
}
