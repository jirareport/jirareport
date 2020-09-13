package br.com.jiratorio.domain.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class EstimateIssueResponse(

    val key: String,

    val summary: String,

    val startDate: String,

    @JsonFormat(pattern = "dd/MM/yyyy")
    val estimateDateAvg: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    val estimateDatePercentile50: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    val estimateDatePercentile75: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    val estimateDatePercentile90: LocalDate,

    val leadTime: Long,

    val issueType: String? = null,

    val creator: String? = null,

    val estimate: String? = null,

    val system: String? = null,

    val project: String? = null,

    val epic: String? = null,

    val priority: String? = null,

    val changelog: List<ChangelogResponse>,

    val impedimentTime: Long,

    val impedimentHistory: List<ImpedimentHistoryResponse>,

    val detailsUrl: String

)
