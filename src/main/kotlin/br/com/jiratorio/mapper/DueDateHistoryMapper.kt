package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.DueDateHistoryResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class DueDateHistoryMapper {

    private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun dueDateHistoryToDueDateHistoryResponse(dueDateHistory: DueDateHistory): DueDateHistoryResponse {
        return DueDateHistoryResponse(
            created = dueDateHistory.created?.format(dateTimePattern),
            dueDate = dueDateHistory.dueDate?.format(datePattern)
        )
    }

    fun dueDateHistoryToDueDateHistoryResponse(dueDateHistory: List<DueDateHistory>?): List<DueDateHistoryResponse>? {
        return dueDateHistory?.map { dueDateHistoryToDueDateHistoryResponse(it) }
    }

}
