package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig
import br.com.jiratorio.domain.impediment.ImpedimentType

data class BoardDetailsResponse(
    var externalId: Long? = null,
    var name: String? = null,
    var startColumn: String? = null,
    var endColumn: String? = null,
    var fluxColumn: List<String>? = null,
    var ignoreIssueType: List<String>? = null,
    var epicCF: String? = null,
    var estimateCF: String? = null,
    var systemCF: String? = null,
    var projectCF: String? = null,
    var ignoreWeekend: Boolean? = null,
    var impedimentType: ImpedimentType? = null,
    var impedimentColumns: List<String>? = null,
    var dynamicFields: List<DynamicFieldConfig>? = null,
    var dueDateCF: String? = null,
    var dueDateType: String? = null
)
