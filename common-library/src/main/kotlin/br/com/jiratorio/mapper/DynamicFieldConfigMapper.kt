package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse

fun DynamicFieldConfigEntity.toDynamicFieldConfigResponse(): DynamicFieldConfigResponse =
    DynamicFieldConfigResponse(
        id = id,
        name = name,
        field = field
    )

fun List<DynamicFieldConfigEntity>.toDynamicFieldConfigResponse(): List<DynamicFieldConfigResponse> =
    map { it.toDynamicFieldConfigResponse() }

fun DynamicFieldConfigRequest.toDynamicFieldConfig(board: BoardEntity): DynamicFieldConfigEntity =
    DynamicFieldConfigEntity(
        board = board,
        name = name,
        field = field
    )
