package br.com.jiratorio.strategy.impediment

import br.com.jiratorio.domain.ImpedimentHistory
import java.time.LocalDateTime

data class InternalImpedimentHistory(
    override val startDate: LocalDateTime,
    override val endDate: LocalDateTime,
    override val leadTime: Long,
) : ImpedimentHistory, Comparable<ImpedimentHistory> {

    override fun compareTo(other: ImpedimentHistory): Int =
        startDate.compareTo(other.startDate)

}
