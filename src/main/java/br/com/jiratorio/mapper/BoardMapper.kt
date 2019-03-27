package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.BoardDetailsResponse
import br.com.jiratorio.domain.response.BoardResponse
import br.com.jiratorio.mapper.transformer.StringTransformer
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component

@Component
class BoardMapper(
    private val stringTransformer: StringTransformer
) {

    fun toBoardResponseDetails(board: Board) =
        BoardDetailsResponse(
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
            impedimentType = board.impedimentType,
            impedimentColumns = board.impedimentColumns,
            dynamicFields = board.dynamicFields,
            dueDateCF = board.dueDateCF,
            dueDateType = board.dueDateType?.name
        )

    fun boardFromCreateBoardRequest(request: CreateBoardRequest) =
        Board(
            name = request.name,
            externalId = request.externalId
        )

    fun toBoardResponse(board: Board) = BoardResponse(
        id = board.id,
        name = board.name,
        owner = board.owner
    )

    fun toBoardResponse(boards: List<Board>) =
        boards.map { toBoardResponse(it) }

    fun toBoardResponse(boards: Page<Board>) =
        PageImpl(
            toBoardResponse(boards.content),
            boards.pageable,
            boards.totalElements
        )

    fun fromUpdateBoardRequest(board: Board, updateBoardRequest: UpdateBoardRequest) =
        board.apply {
            name = updateBoardRequest.name
            startColumn = stringTransformer.toUpperCase(updateBoardRequest.startColumn)
            endColumn = stringTransformer.toUpperCase(updateBoardRequest.endColumn)
            fluxColumn = stringTransformer.listToUpperCase(updateBoardRequest.fluxColumn)
            ignoreIssueType = updateBoardRequest.ignoreIssueType
            epicCF = updateBoardRequest.epicCF
            estimateCF = updateBoardRequest.estimateCF
            systemCF = updateBoardRequest.systemCF
            projectCF = updateBoardRequest.projectCF
            ignoreWeekend = updateBoardRequest.ignoreWeekend
            impedimentType = updateBoardRequest.impedimentType
            impedimentColumns = stringTransformer.listToUpperCase(updateBoardRequest.impedimentColumns)
            dynamicFields = updateBoardRequest.dynamicFields
            touchingColumns = stringTransformer.listToUpperCase(updateBoardRequest.touchingColumns)
            waitingColumns = stringTransformer.listToUpperCase(updateBoardRequest.waitingColumns)
            dueDateCF = updateBoardRequest.dueDateCF
            dueDateType = updateBoardRequest.dueDateType
        }
}
