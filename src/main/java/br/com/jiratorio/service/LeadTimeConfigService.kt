package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse

interface LeadTimeConfigService {

    fun findAllByBoardId(boardId: Long): List<LeadTimeConfig>

    fun findAll(boardId: Long): List<LeadTimeConfigResponse>

    fun create(boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest): Long

    fun findByBoardAndId(boardId: Long, id: Long): LeadTimeConfigResponse

    fun update(boardId: Long, id: Long, leadTimeConfigRequest: LeadTimeConfigRequest)

    fun deleteByBoardAndId(boardId: Long, id: Long)
}
