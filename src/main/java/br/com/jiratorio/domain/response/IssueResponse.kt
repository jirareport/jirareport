package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.entity.embedded.DueDateHistory

data class IssueResponse(
    var id: Long,
    var key: String,

    var creator: String?,

    var summary: String,

    var issueType: String?,
    var estimate: String?,
    var project: String?,
    var epic: String?,
    var system: String?,
    var priority: String?,

    val leadTime: Long,

    var startDate: String,
    var endDate: String,
    var created: String,

    var deviationOfEstimate: Long?,
    var dueDateHistory: List<DueDateHistory>?,

    var impedimentTime: Long?,

    var dynamicFields: Map<String, String?>?,

    var waitTime: Long,
    var touchTime: Long,
    var pctEfficiency: Double
)
