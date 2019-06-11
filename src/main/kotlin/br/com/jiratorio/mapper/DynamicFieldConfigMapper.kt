package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse

fun DynamicFieldConfig.toDynamicFieldConfigResponse(): DynamicFieldConfigResponse =
    DynamicFieldConfigResponse(
        id = id,
        name = name,
        field = field
    )

fun List<DynamicFieldConfig>.toDynamicFieldConfigResponse(): List<DynamicFieldConfigResponse> =
    map { it.toDynamicFieldConfigResponse() }

fun DynamicFieldConfigRequest.toDynamicFieldConfig(board: Board): DynamicFieldConfig =
    DynamicFieldConfig(
        board = board,
        name = name,
        field = field
    )
