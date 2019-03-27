package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigMapper {

    fun toResponse(leadTimeConfig: LeadTimeConfig) =
        LeadTimeConfigResponse(
            id = leadTimeConfig.id,
            boardId = leadTimeConfig.board.id,
            name = leadTimeConfig.name,
            startColumn = leadTimeConfig.startColumn,
            endColumn = leadTimeConfig.endColumn
        )

    fun toResponse(leadTimeConfigs: List<LeadTimeConfig>) =
        leadTimeConfigs.map { toResponse(it) }

    fun toLeadTimeConfig(request: LeadTimeConfigRequest, board: Board) =
        LeadTimeConfig(
            board = board,
            name = request.name,
            startColumn = request.startColumn.toUpperCase(),
            endColumn = request.endColumn.toUpperCase()
        )

    fun updateFromRequest(leadTimeConfig: LeadTimeConfig, request: LeadTimeConfigRequest) =
        leadTimeConfig.apply {
            name = request.name
            startColumn = request.startColumn.toUpperCase()
            endColumn = request.endColumn.toUpperCase()
        }
}
