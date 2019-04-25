package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.extension.toUpperCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component

@Component
class BoardMapper {

    fun toBoardResponseDetails(board: Board): BoardDetailsResponse {
        return BoardDetailsResponse(
            id = board.id,
            externalId = board.externalId,
            name = board.name,
            startColumn = board.startColumn,
            endColumn = board.endColumn,
            fluxColumn = board.fluxColumn,
            ignoreIssueType = board.ignoreIssueType,
            epicCF = board.epicCF,
            estimateCF = board.estimateCF,
            systemCF = board.systemCF,
            projectCF = board.projectCF,
            ignoreWeekend = board.ignoreWeekend,
            dueDateCF = board.dueDateCF,
            dueDateType = board.dueDateType?.name,
            impedimentType = board.impedimentType,
            impedimentColumns = board.impedimentColumns,
            touchingColumns = board.touchingColumns,
            waitingColumns = board.waitingColumns,
            dynamicFields = board.dynamicFields
        )
    }

    fun boardFromCreateBoardRequest(request: CreateBoardRequest): Board {
        return Board(
            name = request.name,
            externalId = request.externalId
        )
    }

    fun toBoardResponse(board: Board): BoardResponse {
        return BoardResponse(
            id = board.id,
            name = board.name,
            owner = board.owner
        )
    }

    fun toBoardResponse(boards: List<Board>): List<BoardResponse> {
        return boards.map { toBoardResponse(it) }
    }

    fun toBoardResponse(boards: Page<Board>): PageImpl<BoardResponse> {
        return PageImpl(
            toBoardResponse(boards.content),
            boards.pageable,
            boards.totalElements
        )
    }

    fun fromUpdateBoardRequest(board: Board, updateBoardRequest: UpdateBoardRequest): Board {
        return board.apply {
            name = updateBoardRequest.name
            startColumn = updateBoardRequest.startColumn?.toUpperCase()
            endColumn = updateBoardRequest.endColumn?.toUpperCase()
            fluxColumn = updateBoardRequest.fluxColumn?.toUpperCase()?.toMutableList()
            ignoreIssueType = updateBoardRequest.ignoreIssueType
            epicCF = updateBoardRequest.epicCF
            estimateCF = updateBoardRequest.estimateCF
            systemCF = updateBoardRequest.systemCF
            projectCF = updateBoardRequest.projectCF
            ignoreWeekend = updateBoardRequest.ignoreWeekend
            impedimentType = updateBoardRequest.impedimentType
            impedimentColumns = updateBoardRequest.impedimentColumns?.toUpperCase()?.toMutableList()
            dynamicFields = updateBoardRequest.dynamicFields
            touchingColumns = updateBoardRequest.touchingColumns?.toUpperCase()?.toMutableList()
            waitingColumns = updateBoardRequest.waitingColumns?.toUpperCase()?.toMutableList()
            dueDateCF = updateBoardRequest.dueDateCF
            dueDateType = updateBoardRequest.dueDateType
        }
    }
}
