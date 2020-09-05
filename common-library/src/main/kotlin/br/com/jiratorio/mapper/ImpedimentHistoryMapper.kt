package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.response.ImpedimentHistoryResponse
import br.com.jiratorio.extension.time.displayFormat

fun ImpedimentHistoryEntity.toImpedimentHistoryResponse(): ImpedimentHistoryResponse =
    ImpedimentHistoryResponse(
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<ImpedimentHistoryEntity>.toImpedimentHistoryResponse(): List<ImpedimentHistoryResponse> =
    map { it.toImpedimentHistoryResponse() }
