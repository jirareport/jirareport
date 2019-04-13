package br.com.jiratorio.domain.dynamicfield

data class DynamicFieldsValues(
    var field: String,
    var values: List<String>? = null
)
