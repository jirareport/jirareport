package br.com.jiratorio.domain.dynamicfield

import java.io.Serializable

data class DynamicFieldConfig(
    var name: String? = null,
    var field: String? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = -8677745120309321988L
    }

}
