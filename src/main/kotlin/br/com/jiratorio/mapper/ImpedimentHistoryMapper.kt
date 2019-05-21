package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.response.ImpedimentHistoryResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class ImpedimentHistoryMapper {

    private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    fun impedimentHistoryToImpedimentHistoryResponse(impedimentHistory: ImpedimentHistory): ImpedimentHistoryResponse {
        return ImpedimentHistoryResponse(
            startDate = impedimentHistory.startDate.format(dateTimePattern),
            endDate = impedimentHistory.endDate.format(dateTimePattern),
            leadTime = impedimentHistory.leadTime
        )
    }

    fun impedimentHistoryToImpedimentHistoryResponse(impedimentHistory: Set<ImpedimentHistory>): List<ImpedimentHistoryResponse> {
        return impedimentHistory.map { impedimentHistoryToImpedimentHistoryResponse(it) }
    }
}
