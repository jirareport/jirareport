package br.com.jiratorio.usecase.jql

import br.com.jiratorio.junit.testtype.UnitTest
import br.com.jiratorio.domain.entity.BoardEntity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@UnitTest
internal class CreateOpenedIssueJqlTest {

    private val openedIssueJql = CreateOpenedIssueJqlUseCase()

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
        BoardEntity(
            name = "test board",
            externalId = 123123L,
            startColumn = "TODO",
            endColumn = "DONE",
            fluxColumn = fluxColumn,
            ignoreIssueType = ignoreIssueType
        )
}
