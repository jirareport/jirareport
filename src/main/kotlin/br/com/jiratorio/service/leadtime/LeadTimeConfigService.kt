package br.com.jiratorio.service.leadtime

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse

interface LeadTimeConfigService {

    fun findAll(boardId: Long): List<LeadTimeConfigResponse>

    fun create(boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest): Long

    fun findByBoardAndId(id: Long, boardId: Long): LeadTimeConfigResponse

    fun update(id: Long, boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest)

    fun deleteByBoardAndId(boardId: Long, id: Long)

}
