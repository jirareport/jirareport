package br.com.jiratorio.domain.estimate

import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

data class EstimateIssue(
    var key: String? = null,
    var issueType: String? = null,
    var creator: String? = null,
    var system: String? = null,
    var epic: String? = null,
    var summary: String? = null,
    var estimated: String? = null,
    var project: String? = null,
    var startDate: LocalDateTime,
    var estimateDateAvg: LocalDate? = null,
    var estimateDatePercentile50: LocalDate? = null,
    var estimateDatePercentile75: LocalDate? = null,
    var estimateDatePercentile90: LocalDate? = null,
    var leadTime: Long? = null,
    var created: LocalDateTime? = null,
    var priority: String? = null,
    var changelog: List<Changelog>? = null,
    var impedimentTime: Long? = null
)
