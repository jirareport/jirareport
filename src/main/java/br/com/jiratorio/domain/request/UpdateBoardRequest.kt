package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.DynamicFieldConfig
import br.com.jiratorio.domain.impediment.ImpedimentType

class UpdateBoardRequest {

    var name: String? = null

    var startColumn: String? = null

    var endColumn: String? = null

    var fluxColumn: MutableList<String>? = null

    var ignoreIssueType: MutableList<String>? = null

    var epicCF: String? = null

    var estimateCF: String? = null

    var systemCF: String? = null

    var projectCF: String? = null

    var ignoreWeekend: Boolean? = null

    var impedimentType: ImpedimentType? = null

    var impedimentColumns: MutableList<String>? = null

    var dynamicFields: MutableList<DynamicFieldConfig>? = null

    var touchingColumns: MutableList<String>? = null

    var waitingColumns: MutableList<String>? = null

    var dueDateCF: String? = null

    var dueDateType: DueDateType? = null

}
