package br.com.jiratorio.jira.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.extension.sanitizeJql
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.testlibrary.extension.toLocalDate
import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@UnitTest
class JiraQueryLanguageServiceTest {

    private val jiraQueryLanguageService = JiraQueryLanguageService()

    @Nested
    inner class FinalizedIssuesTest {

        private val startDate: LocalDate = "01/01/2019".toLocalDate()

        private val endDate: LocalDate = "31/01/2019".toLocalDate()

        @Test
        fun `finalized issues with ignore issue type`() {
            val board = createBoard(mutableListOf("IT_1"))

            val jql = jiraQueryLanguageService.buildFinalizedIssueQuery(board, startDate, endDate)

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
                """.sanitizeJql()
            )
        }

        @Test
        fun `finalized issues without ignore issue type`() {
            val board = createBoard()
            val jql = jiraQueryLanguageService.buildFinalizedIssueQuery(board, startDate, endDate)

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
                """.sanitizeJql()
            )
        }

        @Test
        fun `finalized issues with backslash`() {
            val board = createBoard(
                fluxColumn = mutableListOf("TODO", "WAITING UI\\UX", "UI\\UX DONE", "WIP", "DONE")
            )

            val jql = jiraQueryLanguageService.buildFinalizedIssueQuery(board, startDate, endDate)
            Assertions.assertThat(jql)
                .contains("'WAITING UI\\\\UX'")
                .contains("'UI\\\\UX DONE'")
        }

        @Test
        fun `finalized issues with additionalFilter filter`() {
            val board = createBoard(mutableListOf("IT_1"), additionalFilter = "team = \"jirareport-team\" AND test = 1")

            val jql = jiraQueryLanguageService.buildFinalizedIssueQuery(board, startDate, endDate)

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
               | AND (team = "jirareport-team" AND test = 1) 
                """.sanitizeJql()
            )
        }

    }

    @Nested
    inner class OpenedIssueJqlTest {

        @Test
        fun `opened issues with ignore issue type`() {
            val board = createBoard(mutableListOf("IT_1"))

            val jql = jiraQueryLanguageService.buildOpenedIssueQuery(board)

            Assertions.assertThat(jql)
                .contains("project = '123123'")
                .contains("AND issueType NOT IN ('IT_1')")
                .contains("AND status IN ('TODO','WIP')")
        }

        @Test
        fun `opened issues without ignore issue type`() {
            val board = createBoard()
            val jql = jiraQueryLanguageService.buildOpenedIssueQuery(board)

            Assertions.assertThat(jql)
                .contains("project = '123123'")
                .contains("AND status IN ('TODO','WIP')")
        }

        @Test
        fun `opened issues with backslash`() {
            val board = createBoard(
                fluxColumn = mutableListOf("TODO", "WAITING UI\\UX", "UI\\UX DONE", "WIP", "DONE")
            )

            val jql = jiraQueryLanguageService.buildOpenedIssueQuery(board)
            Assertions.assertThat(jql)
                .contains("'WAITING UI\\\\UX'")
                .contains("'UI\\\\UX DONE'")
        }

        @Test
        fun `opened issues with additionalFilter filter`() {
            val board = createBoard(mutableListOf("IT_1"), additionalFilter = "team = \"jirareport-team\" AND test = 1")

            val jql = jiraQueryLanguageService.buildOpenedIssueQuery(board)

            Assertions.assertThat(jql)
                .contains("project = '123123'")
                .contains("AND issueType NOT IN ('IT_1')")
                .contains("AND status IN ('TODO','WIP')")
                .contains("AND (team = \"jirareport-team\" AND test = 1)")
        }
    }

    fun createBoard(
        ignoreIssueType: MutableList<String>? = null,
        fluxColumn: MutableList<String> = mutableListOf("TODO", "WIP", "DONE"),
        additionalFilter: String? = null
    ) =
        BoardEntity(
            name = "test board",
            externalId = 123123L,
            startColumn = "TODO",
            endColumn = "DONE",
            fluxColumn = fluxColumn,
            ignoreIssueType = ignoreIssueType,
            additionalFilter = additionalFilter
        )
}
