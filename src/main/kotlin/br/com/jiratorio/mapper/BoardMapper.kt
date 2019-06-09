package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardFeatureResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.extension.toUpperCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun Board.toBoardDetailsResponse(): BoardDetailsResponse =
    BoardDetailsResponse(
        id = this.id,
        externalId = this.externalId,
        name = this.name,
        startColumn = this.startColumn,
        endColumn = this.endColumn,
        fluxColumn = this.fluxColumn,
        ignoreIssueType = this.ignoreIssueType,
        epicCF = this.epicCF,
        estimateCF = this.estimateCF,
        systemCF = this.systemCF,
        projectCF = this.projectCF,
        ignoreWeekend = this.ignoreWeekend,
        dueDateCF = this.dueDateCF,
        dueDateType = this.dueDateType?.name,
        impedimentType = this.impedimentType,
        impedimentColumns = this.impedimentColumns,
        touchingColumns = this.touchingColumns,
        waitingColumns = this.waitingColumns,
        feature = BoardFeatureResponse(this)
    )

fun CreateBoardRequest.toBoard(): Board =
    Board(
        name = this.name,
        externalId = this.externalId
    )

fun Board.toBoardResponse(): BoardResponse =
    BoardResponse(
        id = this.id,
        name = this.name,
        owner = this.owner
    )

fun Page<Board>.toBoardResponse(): Page<BoardResponse> =
    PageImpl(
        this.content.map { it.toBoardResponse() },
        this.pageable,
        this.totalElements
    )

fun Board.updateFromRequest(updateBoardRequest: UpdateBoardRequest) {
    this.apply {
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
        touchingColumns = updateBoardRequest.touchingColumns?.toUpperCase()?.toMutableList()
        waitingColumns = updateBoardRequest.waitingColumns?.toUpperCase()?.toMutableList()
        dueDateCF = updateBoardRequest.dueDateCF
        dueDateType = updateBoardRequest.dueDateType
    }
}
