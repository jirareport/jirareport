package br.com.jiratorio.mapper

import br.com.jiratorio.domain.ImpedimentHistory
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.response.ImpedimentHistoryResponse
import br.com.jiratorio.extension.time.displayFormat

fun ImpedimentHistory.toImpedimentHistoryResponse(): ImpedimentHistoryResponse =
    ImpedimentHistoryResponse(
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun ImpedimentHistory.toImpedimentHistoryEntity(): ImpedimentHistoryEntity =
    ImpedimentHistoryEntity(
        startDate = startDate,
        endDate = endDate,
        leadTime = leadTime
    )

fun Set<ImpedimentHistory>.toImpedimentHistoryResponse(): List<ImpedimentHistoryResponse> =
    map { it.toImpedimentHistoryResponse() }

fun Set<ImpedimentHistory>.toImpedimentHistoryEntity(): MutableSet<ImpedimentHistoryEntity> =
    map { it.toImpedimentHistoryEntity() }.toMutableSet()
