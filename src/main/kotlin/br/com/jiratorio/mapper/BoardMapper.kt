package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardFeatureResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun Board.toBoardDetailsResponse(): BoardDetailsResponse =
    BoardDetailsResponse(
        id = id,
        externalId = externalId,
        name = name,
        startColumn = startColumn,
        endColumn = endColumn,
        fluxColumn = fluxColumn,
        ignoreIssueType = ignoreIssueType,
        epicCF = epicCF,
        estimateCF = estimateCF,
        systemCF = systemCF,
        projectCF = projectCF,
        ignoreWeekend = ignoreWeekend,
        dueDateCF = dueDateCF,
        dueDateType = dueDateType?.name,
        impedimentType = impedimentType,
        impedimentColumns = impedimentColumns,
        touchingColumns = touchingColumns,
        waitingColumns = waitingColumns,
        feature = BoardFeatureResponse(this),
        useLastOccurrenceWhenCalculateLeadTime = useLastOccurrenceWhenCalculateLeadTime,
        issuePeriodNameFormat = issuePeriodNameFormat
    )

fun CreateBoardRequest.toBoard(): Board =
    Board(
        name = name,
        externalId = externalId
    )

fun Board.toBoardResponse(): BoardResponse =
    BoardResponse(
        id = id,
        name = name,
        owner = owner
    )

fun Page<Board>.toBoardResponse(): Page<BoardResponse> =
    PageImpl(
        content.map { it.toBoardResponse() },
        pageable,
        totalElements
    )

fun Board.updateFromUpdateBoardRequest(updateBoardRequest: UpdateBoardRequest) {
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
    useLastOccurrenceWhenCalculateLeadTime = updateBoardRequest.useLastOccurrenceWhenCalculateLeadTime
    issuePeriodNameFormat = updateBoardRequest.issuePeriodNameFormat
}
