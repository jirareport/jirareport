package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.DueDateHistoryResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class DueDateHistoryMapper {

    fun dueDateHistoryToDueDateHistoryResponse(dueDateHistory: DueDateHistory): DueDateHistoryResponse {
        return DueDateHistoryResponse(
            created = dueDateHistory.created?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            dueDate = dueDateHistory.dueDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }

    fun dueDateHistoryToDueDateHistoryResponse(dueDateHistory: List<DueDateHistory>?): List<DueDateHistoryResponse>? {
        return dueDateHistory?.map { dueDateHistoryToDueDateHistoryResponse(it) }
    }

}
