package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import br.com.jiratorio.service.DynamicFieldConfigService
import br.com.jiratorio.usecase.dynamicfield.config.CreateDynamicFieldConfig
import br.com.jiratorio.usecase.dynamicfield.config.DeleteDynamicFieldConfig
import br.com.jiratorio.usecase.dynamicfield.config.FindDynamicFieldConfig
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DynamicFieldConfigServiceImpl(
    private val createDynamicField: CreateDynamicFieldConfig,
    private val deleteDynamicField: DeleteDynamicFieldConfig,
    private val findDynamicFieldByBoard: FindDynamicFieldConfig
) : DynamicFieldConfigService {

    @Transactional(readOnly = true)
    override fun findByBoard(boardId: Long): List<DynamicFieldConfigResponse> =
        findDynamicFieldByBoard.execute(boardId)

    @Transactional
    override fun create(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long =
        createDynamicField.create(boardId, dynamicFieldConfigRequest)

    @Transactional
    override fun deleteByBoardAndId(boardId: Long, id: Long) =
        deleteDynamicField.execute(boardId, id)

}
