package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.response.ImpedimentHistoryResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun ImpedimentHistory.toImpedimentHistoryResponse(): ImpedimentHistoryResponse =
    ImpedimentHistoryResponse(
        startDate = startDate.format(dateTimePattern),
        endDate = endDate.format(dateTimePattern),
        leadTime = leadTime
    )

fun Set<ImpedimentHistory>.toImpedimentHistoryResponse(): List<ImpedimentHistoryResponse> =
    map { it.toImpedimentHistoryResponse() }

