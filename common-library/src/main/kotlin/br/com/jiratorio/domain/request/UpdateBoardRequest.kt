package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.issueperiodnameformat.IssuePeriodNameFormat
import javax.validation.constraints.NotBlank

data class UpdateBoardRequest(

    @field:NotBlank
    val name: String,

    val startColumn: String? = null,

    val endColumn: String? = null,

    val fluxColumn: MutableList<String>? = null,

    val ignoreIssueType: MutableList<String>? = null,

    val epicCF: String? = null,

    val estimateCF: String? = null,

    val systemCF: String? = null,

    val projectCF: String? = null,

    val ignoreWeekend: Boolean? = null,

    val impedimentType: ImpedimentType? = null,

    val impedimentColumns: MutableList<String>? = null,

    val touchingColumns: MutableList<String>? = null,

    val waitingColumns: MutableList<String>? = null,

    val dueDateCF: String? = null,

    val dueDateType: DueDateType? = null,

    val useLastOccurrenceWhenCalculateLeadTime: Boolean = false,

    val issuePeriodNameFormat: IssuePeriodNameFormat = IssuePeriodNameFormat.INITIAL_AND_FINAL_DATE

)
