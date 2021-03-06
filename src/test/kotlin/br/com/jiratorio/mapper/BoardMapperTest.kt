package br.com.jiratorio.mapper

import br.com.jiratorio.testlibrary.assertion.response.board.BoardDetailsResponseAssert.Companion.assertThat
import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import org.junit.jupiter.api.Test

@UnitTest
class BoardMapperTest {

    @Test
    fun `test toBoardResponseDetails`() {
        val board = BoardEntity(
            id = 1L,
            externalId = 123L,
            name = "board test",
            startColumn = "START_COLUMN",
            endColumn = "END_COLUMN",
            fluxColumn = mutableListOf("BACKLOG", "START_COLUMN", "WIP_COLUMN", "END_COLUMN"),
            ignoreIssueType = mutableListOf("epic", "sub-task"),
            epicCF = "custom_epic_cf",
            estimateCF = "custom_estimate_cf",
            systemCF = "custom_system_cf",
            projectCF = "custom_project_cf",
            ignoreWeekend = true,
            dueDateCF = "custom_due_date_cf",
            dueDateType = DueDateType.FIRST_DUE_DATE_AND_END_DATE,
            impedimentType = ImpedimentType.COLUMN,
            impedimentColumns = mutableListOf("COLUMN_1", "COLUMN_2"),
            touchingColumns = mutableListOf("TOUCH_1", "TOUCH_2"),
            waitingColumns = mutableListOf("WAITING_1", "WAITING_2")
        )

        val boardDetailsResponse = board.toBoardDetailsResponse()

        assertThat(boardDetailsResponse)
            .hasId(board.id)
            .hasExternalId(board.externalId)
            .hasName(board.name)
            .hasStartColumn(board.startColumn)
            .hasEndColumn(board.endColumn)
            .hasFluxColumn(board.fluxColumn)
            .hasIgnoreIssueType(board.ignoreIssueType)
            .hasEpicCF(board.epicCF)
            .hasEstimateCF(board.estimateCF)
            .hasSystemCF(board.systemCF)
            .hasProjectCF(board.projectCF)
            .hasIgnoreWeekend(board.ignoreWeekend)
            .hasDueDateCF(board.dueDateCF)
            .hasDueDateType(board.dueDateType?.name)
            .hasImpedimentType(board.impedimentType)
            .hasImpedimentColumns(board.impedimentColumns)
            .hasTouchingColumns(board.touchingColumns)
            .hasWaitingColumns(board.waitingColumns)
    }

}
