package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.response.ImpedimentHistoryResponse
import br.com.jiratorio.extension.time.displayFormat
import java.time.format.DateTimeFormatter

fun ImpedimentHistory.toImpedimentHistoryResponse(): ImpedimentHistoryResponse =
    ImpedimentHistoryResponse(
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        leadTime = leadTime
    )

fun Set<ImpedimentHistory>.toImpedimentHistoryResponse(): List<ImpedimentHistoryResponse> =
    map { it.toImpedimentHistoryResponse() }

