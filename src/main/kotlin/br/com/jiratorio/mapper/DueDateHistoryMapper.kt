package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.DueDateHistoryResponse
import br.com.jiratorio.extension.time.displayFormat


fun DueDateHistory.toDueDateHistoryResponse(): DueDateHistoryResponse =
    DueDateHistoryResponse(
        created = created?.displayFormat(),
        dueDate = dueDate?.displayFormat()
    )

fun List<DueDateHistory>.toDueDateHistoryResponse(): List<DueDateHistoryResponse> =
    map { it.toDueDateHistoryResponse() }

