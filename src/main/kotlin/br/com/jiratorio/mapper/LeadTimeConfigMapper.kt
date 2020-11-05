package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse

fun LeadTimeConfigEntity.toLeadTimeConfigResponse(): LeadTimeConfigResponse =
    LeadTimeConfigResponse(
        id = id,
        boardId = board.id,
        name = name,
        startColumn = startColumn,
        endColumn = endColumn
    )

fun List<LeadTimeConfigEntity>.toLeadTimeConfigResponse(): List<LeadTimeConfigResponse> =
    map { it.toLeadTimeConfigResponse() }

fun LeadTimeConfigRequest.toLeadTimeConfig(board: BoardEntity): LeadTimeConfigEntity =
    LeadTimeConfigEntity(
        board = board,
        name = name,
        startColumn = startColumn.toUpperCase(),
        endColumn = endColumn.toUpperCase()
    )

fun LeadTimeConfigEntity.updateFromLeadTimeConfigRequest(request: LeadTimeConfigRequest) {
    name = request.name
    startColumn = request.startColumn.toUpperCase()
    endColumn = request.endColumn.toUpperCase()
}
