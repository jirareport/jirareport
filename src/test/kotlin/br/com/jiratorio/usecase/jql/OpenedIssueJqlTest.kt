package br.com.jiratorio.usecase.jql

import br.com.jiratorio.domain.entity.Board
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class OpenedIssueJqlTest {

    private val openedIssueJql = OpenedIssueJql()

    @Test
    fun `opened issues with ignore issue type`() {
        val board = createBoard(mutableListOf("IT_1"))

        val jql = openedIssueJql.execute(board)

        Assertions.assertThat(jql)
            .contains("project = '123123'")
            .contains("AND issueType NOT IN ('IT_1')")
            .contains("AND status IN ('TODO','WIP')")
    }

    @Test
    fun `opened issues without ignore issue type`() {
        val board = createBoard()
        val jql = openedIssueJql.execute(board)

        Assertions.assertThat(jql)
            .contains("project = '123123'")
            .contains("AND status IN ('TODO','WIP')")
    }

    @Test
    fun `opened issues with backslash`() {
        val board = createBoard(
            fluxColumn = mutableListOf("TODO", "WAITING UI\\UX", "UI\\UX DONE", "WIP", "DONE")
        )

        val jql = openedIssueJql.execute(board)
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
