package br.com.jiratorio.domain.response.board

import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.impediment.ImpedimentType

data class BoardDetailsResponse(
    val id: Long,
    val externalId: Long? = null,
    val name: String? = null,
    val startColumn: String? = null,
    val endColumn: String? = null,
    val fluxColumn: List<String>? = null,
    val ignoreIssueType: List<String>? = null,
    val epicCF: String? = null,
    val estimateCF: String? = null,
    val systemCF: String? = null,
    val projectCF: String? = null,
    val ignoreWeekend: Boolean? = null,
    val dueDateCF: String? = null,
    val dueDateType: String? = null,
    val impedimentType: ImpedimentType? = null,
    val impedimentColumns: List<String>? = null,
    val touchingColumns: List<String>? = null,
    val waitingColumns: List<String>? = null
)
