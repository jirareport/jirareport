package br.com.jiratorio.domain.form

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import java.time.LocalDate
import java.util.ArrayList

data class IssueForm(

    var startDate: LocalDate? = null,

    var endDate: LocalDate? = null,

    var keys: MutableList<String?> = ArrayList(),

    var systems: MutableList<String?> = ArrayList(),

    var taskSize: MutableList<String?> = ArrayList(),

    var epics: MutableList<String?> = ArrayList(),

    var issueTypes: MutableList<String?> = ArrayList(),

    var projects: MutableList<String?> = ArrayList(),

    var priorities: MutableList<String?> = ArrayList(),

    var dynamicFieldsValues: MutableList<DynamicFieldsValues?> = ArrayList()

)
