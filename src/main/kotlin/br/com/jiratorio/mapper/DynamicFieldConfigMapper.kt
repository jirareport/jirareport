package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import org.springframework.stereotype.Component

@Component
class DynamicFieldConfigMapper {

    fun dynamicFieldConfigToDynamicFieldConfigResponse(dynamicField: DynamicFieldConfig): DynamicFieldConfigResponse {
        return DynamicFieldConfigResponse(
            id = dynamicField.id,
            name = dynamicField.name,
            field = dynamicField.field
        )
    }

    fun dynamicFieldConfigToDynamicFieldConfigResponse(dynamicFields: List<DynamicFieldConfig>): List<DynamicFieldConfigResponse> {
        return dynamicFields.map { dynamicFieldConfigToDynamicFieldConfigResponse(it) }
    }

    fun dynamicFieldConfigRequestToDynamicFieldConfig(
        board: Board,
        dynamicFieldConfigRequest: DynamicFieldConfigRequest
    ): DynamicFieldConfig {
        return DynamicFieldConfig(
            board = board,
            name = dynamicFieldConfigRequest.name,
            field = dynamicFieldConfigRequest.field
        )
    }

}
