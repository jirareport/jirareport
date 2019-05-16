package br.com.jiratorio.domain.estimate

import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

data class EstimateIssue(

    val key: String,

    val summary: String,

    val startDate: LocalDateTime,

    var estimateDateAvg: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile50: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile75: LocalDate = startDate.toLocalDate(),

    var estimateDatePercentile90: LocalDate = startDate.toLocalDate(),

    val leadTime: Long,

    val created: LocalDateTime,

    val issueType: String? = null,

    val creator: String? = null,

    val estimate: String? = null,

    val system: String? = null,

    val project: String? = null,

    val epic: String? = null,

    val priority: String? = null,

    val changelog: List<Changelog>,

    val impedimentTime: Long

)
