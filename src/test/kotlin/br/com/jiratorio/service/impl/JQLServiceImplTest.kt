package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.extension.toLocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag("unit")
internal class JQLServiceImplTest {

    private val jqlService: JQLServiceImpl = JQLServiceImpl()

    @Nested
    inner class FinalizedIssues {

        private val startDate: LocalDate = "01/01/2019".toLocalDate()

        private val endDate: LocalDate = "31/01/2019".toLocalDate()

        @Test
        fun `finalized issues with ignore issue type`() {
            val board = createBoard(mutableListOf("IT_1"))

            val jql = jqlService.finalizedIssues(board, startDate, endDate)

            assertThat(jql).isEqualTo(
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
            val jql = jqlService.finalizedIssues(board, startDate, endDate)

            assertThat(jql).isEqualTo(
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

            val jql = jqlService.finalizedIssues(board, startDate, endDate)
            assertThat(jql)
                .contains("'WAITING UI\\\\UX'")
                .contains("'UI\\\\UX DONE'")
        }

    }

    @Nested
    inner class OpenedIssue {

        @Test
        fun `opened issues with ignore issue type`() {
            val board = createBoard(mutableListOf("IT_1"))

            val jql = jqlService.openedIssues(board)

            assertThat(jql)
                .contains("project = '123123'")
                .contains("AND issueType NOT IN ('IT_1')")
                .contains("AND status IN ('TODO','WIP')")
        }

        @Test
        fun `opened issues without ignore issue type`() {
            val board = createBoard()
            val jql = jqlService.openedIssues(board)

            assertThat(jql)
                .contains("project = '123123'")
                .contains("AND status IN ('TODO','WIP')")
        }

        @Test
        fun `opened issues with backslash`() {
            val board = createBoard(
                fluxColumn = mutableListOf("TODO", "WAITING UI\\UX", "UI\\UX DONE", "WIP", "DONE")
            )

            val jql = jqlService.openedIssues(board)
            assertThat(jql)
                .contains("'WAITING UI\\\\UX'")
                .contains("'UI\\\\UX DONE'")
        }

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
