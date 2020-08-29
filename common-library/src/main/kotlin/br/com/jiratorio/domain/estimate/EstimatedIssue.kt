package br.com.jiratorio.domain.estimate

import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.domain.entity.ImpedimentHistory
import java.time.LocalDate
import java.time.LocalDateTime

data class EstimatedIssue(

    val key: String,

    val summary: String,

    val startDate: LocalDateTime,

    var estimateDateAvg: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile50: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile75: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile90: LocalDate = startDate.toLocalDate(),

    val leadTime: Long,

    val issueType: String? = null,

    val creator: String? = null,

    val estimate: String? = null,

    val system: String? = null,

    val project: String? = null,

    val epic: String? = null,

    val priority: String? = null,

    val columnChangelog: Set<ColumnChangelog>,

    val impedimentTime: Long,

    val impedimentHistory: Set<ImpedimentHistory>

)
