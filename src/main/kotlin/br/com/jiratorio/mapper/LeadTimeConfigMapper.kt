package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse

fun LeadTimeConfig.toLeadTimeConfigResponse(): LeadTimeConfigResponse =
    LeadTimeConfigResponse(
        id = id,
        boardId = board.id,
        name = name,
        startColumn = startColumn,
        endColumn = endColumn
    )

fun List<LeadTimeConfig>.toLeadTimeConfigResponse(): List<LeadTimeConfigResponse> =
    map { it.toLeadTimeConfigResponse() }

fun LeadTimeConfigRequest.toLeadTimeConfig(board: Board): LeadTimeConfig =
    LeadTimeConfig(
        board = board,
        name = name,
        startColumn = startColumn.toUpperCase(),
        endColumn = endColumn.toUpperCase()
    )

fun LeadTimeConfig.updateFromLeadTimeConfigRequest(request: LeadTimeConfigRequest) {
    name = request.name
    startColumn = request.startColumn.toUpperCase()
    endColumn = request.endColumn.toUpperCase()
}
