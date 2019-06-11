package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.DueDateHistoryResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun DueDateHistory.toDueDateHistoryResponse(): DueDateHistoryResponse =
    DueDateHistoryResponse(
        created = created?.format(dateTimePattern),
        dueDate = dueDate?.format(datePattern)
    )

fun List<DueDateHistory>.toDueDateHistoryResponse(): List<DueDateHistoryResponse> =
    map { it.toDueDateHistoryResponse() }

