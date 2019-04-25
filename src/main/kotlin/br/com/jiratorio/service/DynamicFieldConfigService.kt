package br.com.jiratorio.service

import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse

interface DynamicFieldConfigService {

    fun findByBoard(boardId: Long): List<DynamicFieldConfigResponse>

    fun create(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long

    fun deleteByBoardAndId(boardId: Long, id: Long)

}
