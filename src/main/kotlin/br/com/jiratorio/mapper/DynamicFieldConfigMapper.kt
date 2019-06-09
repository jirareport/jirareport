package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse

fun DynamicFieldConfig.toDynamicFieldConfigResponse(): DynamicFieldConfigResponse =
    DynamicFieldConfigResponse(
        id = this.id,
        name = this.name,
        field = this.field
    )

fun List<DynamicFieldConfig>.toDynamicFieldConfigResponse(): List<DynamicFieldConfigResponse> =
    this.map { it.toDynamicFieldConfigResponse() }

fun DynamicFieldConfigRequest.toDynamicFieldConfig(board: Board): DynamicFieldConfig =
    DynamicFieldConfig(
        board = board,
        name = this.name,
        field = this.field
    )
