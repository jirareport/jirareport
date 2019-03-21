package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.mapper.transformer.StringTransformer
import org.springframework.stereotype.Component

@Component
class LeadTimeConfigMapper(
    private val stringTransformer: StringTransformer
) {

    fun toResponse(leadTimeConfig: LeadTimeConfig) =
        LeadTimeConfigResponse(
            id = leadTimeConfig.id!!,
            boardId = leadTimeConfig.board?.id!!,
            name = leadTimeConfig.name!!,
            startColumn = leadTimeConfig.startColumn!!,
            endColumn = leadTimeConfig.endColumn!!
        )

    fun toResponse(leadTimeConfigs: List<LeadTimeConfig>) =
        leadTimeConfigs.map { toResponse(it) }

    fun toLeadTimeConfig(request: LeadTimeConfigRequest, boardId: Long) =
        LeadTimeConfig().apply {
            board = Board(boardId)
            name = request.name
            startColumn = stringTransformer.toUpperCase(request.startColumn)
            endColumn = stringTransformer.toUpperCase(request.endColumn)
        }

    fun updateFromRequest(leadTimeConfig: LeadTimeConfig, request: LeadTimeConfigRequest) =
        leadTimeConfig.apply {
            name = request.name
            startColumn = stringTransformer.toUpperCase(request.startColumn)
            endColumn = stringTransformer.toUpperCase(request.endColumn)
        }
}
